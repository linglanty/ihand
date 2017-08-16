package cn.com.dj.biz;

import cn.com.dj.dao.HeartBeatDao;
import cn.com.dj.dao.TaskLockDao;
import cn.com.dj.dto.heartbeat.HeartBeatInfo;
import cn.com.dj.service.AutoHeartBeatService;
import cn.com.dj.service.DeviceService;
import cn.com.dj.task.RpcClient;
import cn.com.dj.util.DateUtils;
import cn.com.dj.util.MachineUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.com.dj.controller.HomeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cn.com.dj.task.DetectService;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  ***** 此类为任务调度器: 负责分配任务 ******
 *  假如: 定时任务每5分钟执行一次, 共有3台机器(a, b, c)来执行定时任务
 *  0 : 为了避免任务调度器为单个节点带来的不可靠性, 引入多节点抢占式执行任务
 *  具体逻辑如下:
 *      每个执行任务的机器也是任务调度器, 定时任务执行时,先去数据库中拿锁,如果拿到了锁则执行任务调度,否则不执行。(保证一个时间间隔内只会有一个定时任务执行)
 *       锁的设计:
 *       (ip, lock_time) lock_time为上一次任务调度开始的时间, ip为上一次执行任务调度的机器
 *         机器a 去数据库中执行 "update lock_time = now, ip = a where lock_time < now - 5分钟"
 *         如果执行成功, 则开始执行任务调度;
 *         机器b 同一时间也去数据库中执行 "update lock_time = now, ip = a where lock_time < now - 5分钟",
 *         而此时的条件 "lock_time < now - 5分钟" 不成立, 机器b则不会执行任务调度。
 *
 *   这样一来, 保证了同一时间周期内只会有一台机器执行任务调度, 任意一台定时任务机器挂了 也不会影响整个定时任务工作
 *
 *  1: 获取所有需要执行的任务列表
 *
 *  2: 任务分配:
 *      1. 获取所有可以执行定时任务的机器:
 *         - 1. 每台机器都会执行 {@link AutoHeartBeatService} 中的定时任务, 每五秒钟向数据库(表" heat_beat ")中上报自己的状态(ip, cpu利用率)
 *         - 2.1 调度器获取所有在过去20秒内上报自己状态的机器(a,b,c) 通过执行Sql(SELECT ip, date, cpu_rate AS cpuRate FROM heat_beat where date > now - 20秒)
 *         - 2.2 调度器在给这些机器分配任务前,会再一次检查下他们的状态(是否能响应), 若不能响应则不对它分配任务
 *          2.1, 2.2 在方法{@link TaskScheduler#getOnlineMachines}中
 *         - 3. 根据每台机机器的 cpu利用率 来为每个机器分配任务 方法{@link TaskScheduler#allocateTasks}
 *         - 4. 将任务列表分别发送给每台机器 方法{@link TaskScheduler#sendTasks}
 *
 *  3. 每个执行任务的机器接受任务的地方:{@link HomeController#execTasks}
 *
 *  4. 单个任务的执行在 {@link DetectService#detectFaults}
 *
 * Created by dujiang02 on 17/7/23.
 */
@Service
public class TaskScheduler {
    private Logger logger = LoggerFactory.getLogger(TaskScheduler.class);

    @Autowired
    private HeartBeatDao heartBeatDao;

    @Autowired
    private RpcClient rpcClient;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private TaskLockDao taskLockDao;

    @Value("${config.detect.oid}")
    private String oId;

    //定时任务每隔5分钟执行一次
    public void execAllTasks() {
        // step0:所有机器抢占式执行定时任务
        String ip;
        try {
            ip = MachineUtils.getIpAddress();
        } catch (UnknownHostException e) {
            logger.error("Get Machine Ip Info error ", e);
            return;
        }
        Date lastTaskTime = DateTime.now().plusMinutes(-5).toDate();
        if (taskLockDao.lock(lastTaskTime, ip) < 1) {
            logger.info("ip: " + ip + " haven't lock the task, time : " + new Date());
            return;
        }

        // step1: 获取所有需要执行的任务队列
        List<ObjectId> onlineDevices = deviceService.getOnlineDevices(new ObjectId(oId));

        // step2 : 把所有的能执行定时任务的机器给找出来
        Date date = DateUtils.bottomSecond(20);
        List<HeartBeatInfo> onlineMachines = getOnlineMachines(date);
        // step3 : 为每个机器分配任务
        Map<HeartBeatInfo, List<ObjectId>> tasks = allocateTasks(onlineMachines, onlineDevices);
        for (Map.Entry<HeartBeatInfo, List<ObjectId>> task : tasks.entrySet()) {
            //step4: 告诉每个机器去执行定时任务
            sendTasks(task.getKey(), task.getValue());
        }
    }

    //分配任务
    private Map<HeartBeatInfo, List<ObjectId>> allocateTasks (List<HeartBeatInfo> onlineMachines, List<ObjectId> onlineDevices) {
        int taskCount = onlineDevices.size();
        double sum = 0;
        for (HeartBeatInfo machine : onlineMachines) {
            sum  = sum + computeCpuUsage(machine);
        }
        Map<HeartBeatInfo, List<ObjectId>> taskMap = Maps.newHashMap();
        int index = 0;
        for (HeartBeatInfo machine : onlineMachines) {
            double taskPercentage = computeCpuUsage(machine)/sum;
            int end = Math.min(index + (int) Math.round(taskPercentage), taskCount);
            if (index >= end) {
                break;
            }
            taskMap.put(machine, onlineDevices.subList(index, end));
        }
        return taskMap;
    }

    private void sendTasks(HeartBeatInfo machine, List<ObjectId> objectIds) {
        if (CollectionUtils.isEmpty(objectIds)) {
            return;
        }
        String url = machine.getIp() +":8080/" + "api/task/execTasks";
        rpcClient.sendTasks(url, objectIds);
    }

    private double computeCpuUsage(HeartBeatInfo machine) {
       return  (1.0/(machine.getCpuRate() + 0.001));
    }

    private List<HeartBeatInfo> getOnlineMachines(Date date) {
        List<HeartBeatInfo> validMachines = heartBeatDao.getValidMachine(date);
        if (CollectionUtils.isEmpty(validMachines)) {
            return Lists.newArrayList();
        }
        List<HeartBeatInfo> heartBeatInfos = Lists.newArrayListWithCapacity(validMachines.size());
        for (HeartBeatInfo machine : heartBeatInfos) {
            String url = machine.getIp() +":8080/" + "api/task/listcheck";
            boolean isWork = rpcClient.checkMachine(url);
            if (isWork) {
                heartBeatInfos.add(machine);
            }
        }
        return heartBeatInfos;
    }


}

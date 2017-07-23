package cn.com.dj.biz;

import cn.com.dj.dao.HeartBeatDao;
import cn.com.dj.dto.heartbeat.HeartBeatInfo;
import cn.com.dj.service.DeviceService;
import cn.com.dj.task.RpcClient;
import cn.com.dj.util.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dujiang02 on 17/7/23.
 */
@Service
public class TaskScheduler {


    @Autowired
    private HeartBeatDao heartBeatDao;

    @Autowired
    private RpcClient rpcClient;

    @Autowired
    private DeviceService deviceService;

    @Value("${config.detect.oid}")
    private String oId;

    public void execAllTasks() {
        // step1 :把所有的能执行定时任务的机器给找出来
        Date date = DateUtils.bottomSecond(20);
        List<HeartBeatInfo> onlineMachines = getOnlineMachines(date);

        // step2: 获取所有需要执行的任务队列
        List<ObjectId> onlineDevices = deviceService.getOnlineDevices(new ObjectId(oId));

        // step3:为每个机器分配任务
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

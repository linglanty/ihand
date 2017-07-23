package cn.com.dj.service;

import cn.com.dj.dao.HeartBeatDao;
import cn.com.dj.dto.heartbeat.HeartBeatInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 每台机器自动化地上报自己的数据到数据库
 * Created by dujiang02 on 17/7/23.
 */
@Service
public class AutoHeartBeatService implements InitializingBean{
    private Logger logger = LoggerFactory.getLogger(AutoHeartBeatService.class);
    //使用定时任务线程池
    private static final ScheduledExecutorService batchService = Executors.newScheduledThreadPool(1);

    @Autowired
    private HeartBeatDao heartBeatDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        batchService.scheduleAtFixedRate(new Runnable() {
            @Override public void run() {
                try {
                    HeartBeatInfo heartBeat =new HeartBeatInfo();
                    heartBeat.setCpuRate(getCpuUsageRate());
                    heartBeat.setIp(getIpAddress());
                    heartBeat.setDate(new Date());
                    heartBeatDao.submitHeatBeatInfo(heartBeat);
                } catch (Exception e) {
                    logger.error("上报心跳信息失败", e);
                    return;
                }

            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    //获取Ip地址
    private String getIpAddress() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        return addr.getHostAddress();
    }

    private double getCpuUsageRate(){
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        return osBean.getSystemLoadAverage();
    }
}

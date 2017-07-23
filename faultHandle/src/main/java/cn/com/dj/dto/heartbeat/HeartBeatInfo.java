package cn.com.dj.dto.heartbeat;

import java.util.Date;

/**
 * 心跳相关信息:
 *  1. 用于分布式执行定时任务
 * Created by dujiang02 on 17/7/23.
 */
public class HeartBeatInfo {

    private String ip;
    private Date date;
    private double cpuRate;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCpuRate() {
        return cpuRate;
    }

    public void setCpuRate(double cpuRate) {
        this.cpuRate = cpuRate;
    }
}

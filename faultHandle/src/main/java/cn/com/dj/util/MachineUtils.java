package cn.com.dj.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dujiang02 on 17/8/16.
 */
public class MachineUtils {

    //获取Ip地址
    public static String getIpAddress() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        return addr.getHostAddress();
    }

    //获取当前Cpu使用率
    public static double getCpuUsageRate(){
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        return osBean.getSystemLoadAverage();
    }
}

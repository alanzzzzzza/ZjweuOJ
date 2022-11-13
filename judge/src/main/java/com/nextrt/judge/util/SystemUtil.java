package com.nextrt.judge.util;

import com.nextrt.judge.vo.SystemInfo;
import com.sun.management.UnixOperatingSystemMXBean;
import org.springframework.util.DigestUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SystemUtil {
    public static SystemInfo getSystemInfo(){
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        SystemInfo systemInfo = new SystemInfo();
        if (os instanceof UnixOperatingSystemMXBean) {
            UnixOperatingSystemMXBean unixOs = (UnixOperatingSystemMXBean) os;
            systemInfo.setCpuCoreNum(unixOs.getAvailableProcessors());
            systemInfo.setSysInfo(unixOs.getName() + "-" + unixOs.getArch() + "-" + unixOs.getVersion());
            systemInfo.setCpu((double) Math.round((unixOs.getSystemCpuLoad() * 100)* 100) / 100);
            systemInfo.setMemory(unixOs.getTotalPhysicalMemorySize());
            systemInfo.setMemoryFree(unixOs.getFreePhysicalMemorySize());
            systemInfo.setMem((double) Math.round(((double) (systemInfo.getMemory() - systemInfo.getMemoryFree()) / systemInfo.getMemory() * 100) * 100) / 100 );
        }
        systemInfo.setSn(getSN());
        return systemInfo;
    }
    public static String getSN(){
        String str = getLocalIp();
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
    public static String getLocalIp(){
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
        return ia.toString().split("/")[1];
    }
}

package com.nextrt.acm.util;

import cn.hutool.core.util.StrUtil;
import com.github.jarod.qqwry.QQWry;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class NetUtil {
    public static int checkContestIP(String checkPublicIP, String PublicIPLimit) {
        if (!StrUtil.hasBlank(PublicIPLimit)) {
            if (!cn.hutool.core.net.NetUtil.isInRange(checkPublicIP, PublicIPLimit))
                return 0;
        }
        return 1;
    }

    public static String getIPInfo(String ip) {
        try {
            QQWry qqwry = new QQWry();
            return qqwry.findIP(ip).toString().replace("CZ88.NET", "");
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getPublicIP(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.split(",").length > 0)
            return ip.split(",")[0];
        else
            return ip;
    }
}

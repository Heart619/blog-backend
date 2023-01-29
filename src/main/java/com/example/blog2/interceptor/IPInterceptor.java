package com.example.blog2.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mxp
 * @date 2023/1/29 13:08
 */
@Component
public class IPInterceptor implements HandlerInterceptor {

    private final Set<String> blackIP = new HashSet<>(Arrays.asList(
            // 添加要加入黑名单的ip
    ));

    public static final ThreadLocal<String> IP_INFO = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = getIpAddress(request);
        if (blackIP.contains(ipAddress)) {
            return false;
        }
        IP_INFO.set(ipAddress);
        return true;
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.indexOf (",") > 0) {
                ip = ip.substring (0, ip.indexOf (","));
            }
            if (ip.equals ("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost ();
                } catch (Exception e) {
                    e.printStackTrace ();
                }
                ip = inet.getHostAddress ();
            }
        }

        if (request.getHeader("X-Real-IP") != null && !"".equals(request.getHeader("X-Real-IP")) && !"unknown".equalsIgnoreCase(request.getHeader("X-Real-IP"))) {
            ip = request.getHeader("X-Real-IP");
        }

        if (request.getHeader("Proxy-Client-IP") != null && !"".equals(request.getHeader("Proxy-Client-IP")) && !"unknown".equalsIgnoreCase(request.getHeader("Proxy-Client-IP"))) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (request.getHeader("WL-Proxy-Client-IP") != null && !"".equals(request.getHeader("WL-Proxy-Client-IP")) && !"unknown".equalsIgnoreCase(request.getHeader("WL-Proxy-Client-IP"))) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

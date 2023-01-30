package com.example.blog2.interceptor;

import com.example.blog2.utils.IPUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        String ipAddress = IPUtils.getIpAddress(request);
        if (blackIP.contains(ipAddress)) {
            return false;
        }
        IP_INFO.set(ipAddress);
        return true;
    }
}

package com.example.blog2.filter;

import com.example.blog2.utils.IPUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
@javax.servlet.annotation.WebFilter(filterName = "sessionFilter", urlPatterns = "/websocket/*")
@Order(1)
public class WebSocketFilter implements Filter {

    public static final ThreadLocal<String> IP_INFO = new ThreadLocal<>();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        IP_INFO.set(IPUtils.getIpAddress(req));
        filterChain.doFilter(servletRequest, servletResponse);
        IP_INFO.remove();
    }
}

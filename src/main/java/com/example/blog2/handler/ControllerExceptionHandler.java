package com.example.blog2.handler;

import com.example.blog2.interceptor.IPInterceptor;
import com.example.blog2.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 拦截到所有名字具有Controller的控制器
 * @author mxp
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.example.blog2.controller")
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    private R exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
            throw e;
        }
        log.error("IP : {}, Request URL : {}, Exception : {}", IPInterceptor.IP_INFO.get(), request.getRequestURL(), e.getMessage());
        e.printStackTrace();
        if (!StringUtils.isEmpty(e.getMessage())) {
            return R.error(e.getMessage());
        }
        return R.error("网络繁忙，请稍后再试");
    }
}

package com.example.blog2.config;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/14 15:18
 */
import com.example.blog2.interceptor.IPInterceptor;
import com.example.blog2.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * token拦截
 * @author mxp
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Autowired
    private IPInterceptor ipInterceptor;

    @Autowired
    private ThreadPoolExecutor executors;

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer){
        configurer.setTaskExecutor(new ConcurrentTaskExecutor(executors));
        configurer.setDefaultTimeout(30000);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //排除拦截，除了注册登录(此时还没token)，其他都拦截
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/admin/**")
                .order(0);

        registry.addInterceptor(ipInterceptor)
                .addPathPatterns("/**")
                .order(1);
    }
}

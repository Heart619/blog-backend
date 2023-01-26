package com.example.blog2.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mxp
 * @date 2023/1/25 20:39
 */
@EnableConfigurationProperties(ThreadProperties.class)
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadProperties properties) {
        return new ThreadPoolExecutor(
                properties.getCore(),
                properties.getMax(),
                15,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}

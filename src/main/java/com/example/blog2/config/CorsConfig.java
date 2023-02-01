package com.example.blog2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    static final String[] ORIGINS = new String[]{"GET", "POST", "PUT", "DELETE", "OPTIONS"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 所有的当前站点的请求地址，都支持跨域访问。
        registry.addMapping("/**")
                //是否发送Cookie
                .allowCredentials(true)
                //放行哪些原始域
                .allowedOriginPatterns("*")
                //当前站点支持的跨域请求类型是什么
                .allowedMethods(ORIGINS)
                //	允许请求头中的header，默认都支持
                .allowedHeaders("*")
                //响应头中允许访问的header，默认为空
                .exposedHeaders("*")
                //预请求的结果的有效期，默认30分钟,这里为一天
                .maxAge(24 * 60 * 60);
    }
}

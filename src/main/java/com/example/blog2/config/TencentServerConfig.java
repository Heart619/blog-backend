package com.example.blog2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author mxp
 * @date 2023/1/29 12:30
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "three.location")
public class TencentServerConfig {

    private String key;
}

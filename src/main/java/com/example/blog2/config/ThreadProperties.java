package com.example.blog2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author mxp
 * @date 2023/1/25 20:39
 */
@Data
@ConfigurationProperties(prefix = "my.thread")
public class ThreadProperties {
    private Integer core;
    private Integer max;
}

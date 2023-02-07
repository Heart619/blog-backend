package com.example.blog2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author mxp
 * @date 2023/1/23 20:37
 */
@ConfigurationProperties(prefix = "three.oss")
@Data
public class OSSConfig {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String region;

    private String address;
    private String imgSrc;
    private String textSrc;
    private String otherSrc;
}

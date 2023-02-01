package com.example.blog2.controller.three;

import com.example.blog2.config.OSSConfig;
import com.example.blog2.to.UploadTokenTo;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @author mxp
 * @date 2023/1/23 20:40
 */
@EnableConfigurationProperties(OSSConfig.class)
@RestController
public class OssController {

    @Autowired
    private OSSConfig ossConfig;

    @GetMapping("oss/policy")
    public UploadTokenTo getToken() {
        Auth auth = Auth.create(ossConfig.getAccessKey(), ossConfig.getSecretKey());
        UploadTokenTo token = new UploadTokenTo();
        token.setToken(auth.uploadToken(ossConfig.getBucket()));
        token.setDir(ossConfig.getImgSrc());
        return token;
    }
}

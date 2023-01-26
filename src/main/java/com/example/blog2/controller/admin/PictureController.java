package com.example.blog2.controller.admin;

import com.alibaba.fastjson.JSONException;
import com.example.blog2.config.OSSConfig;
import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/5 21:14
 */
@Slf4j
//@RestController
public class PictureController {

    @Autowired
    private OSSConfig ossConfig;

    private static final String prefix = "blog/" + LocalDate.now() + "/";

    @PostMapping(value = "/upload")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new Result(true, StatusCode.ERROR,"上传图片为空",null);
        }
        String fileName = file.getOriginalFilename();//上传的文件名
        fileName = prefix + UUID.randomUUID() + "_" + fileName;//生成唯一文件名
        try {
            byte[] fileBytes = file.getBytes();//转换为byte数组
            String k = upload(fileName, fileBytes);
            if (k == null) {
                return new Result(true, StatusCode.ERROR,"上传图片失败",null);
            }
            return new Result(true, StatusCode.OK,"上传图片成功", ossConfig.getRegion() + "/" + k);
        } catch (IOException | JSONException e) {

        }
        return new Result(true, StatusCode.ERROR,"上传图片失败",null);
    }

    private String upload(String fileName, byte[] bytes) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = ossConfig.getAccessKey();
        String secretKey = ossConfig.getSecretKey();
        String bucket = ossConfig.getBucket();
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error(r.toString());
            try {
                log.error(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return null;
    }
}

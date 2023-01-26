package com.example.blog2.utils;

import com.example.blog2.config.OSSConfig;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author mxp
 * @date 2023/1/24 21:46
 */
@Slf4j
@Component
public class OSSUtils {

    @Autowired
    private OSSConfig ossConfig;

    public String uploadText(String key, byte[] bytes) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = ossConfig.getAccessKey();
        String secretKey = ossConfig.getSecretKey();
        String bucket = ossConfig.getBucket();

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
//默认不指定key的情况下，以文件内容的hash值作为文件名
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

    public void delText(String key) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        Auth auth = Auth.create(ossConfig.getAccessKey(), ossConfig.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(ossConfig.getBucket(), key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
        }
    }

    public byte[] loadText(String key) {
        try {
            String domainOfBucket = ossConfig.getRegion();
            String encodedFileName = URLEncoder.encode(key, "utf-8").replace("+", "%20");
            String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
            URL url = new URL(finalUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            BufferedInputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            FileOutputStream fileOutputStream = new FileOutputStream("D:/test.jpg");
            byte[] t = new byte[1024 * 1024];
            int b;
            while ((b = inputStream.read(t)) != -1) {
                byteArrayOutputStream.write(t, 0, b);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

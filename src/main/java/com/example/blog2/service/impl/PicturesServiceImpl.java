package com.example.blog2.service.impl;

import com.example.blog2.config.OSSConfig;
import com.example.blog2.dao.PicturesRepository;
import com.example.blog2.po.Pictures;
import com.example.blog2.service.PicturesService;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author mxp
 * @date 2023/1/24 19:26
 */
@Service
public class PicturesServiceImpl implements PicturesService {

    @Autowired
    private PicturesRepository picturesRepository;

    @Autowired
    private OSSConfig ossConfig;

    @Transactional
    @Override
    public Pictures saveType(Pictures pictures) {
        return picturesRepository.save(pictures);
    }

    @Override
    public Page<Pictures> listType(Pageable pageable) {
        return null;
    }

    @Override
    public List<Pictures> listType() {
        return picturesRepository.findAll();
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteType(Long id, String key) {
        try {
            delOssImg(key);
            picturesRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delOssImg(String key) {
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
}

package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.entity.UserEntity;
import com.example.blog2.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * @author mxp
 * @date 2023-01-25 09:47:20
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获得所有用户省份信息
     * @return
     */
    List<UserEntity> getUserAreaList();
}


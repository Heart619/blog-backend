package com.example.blog2.service.impl;

import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.UserDao;
import com.example.blog2.entity.UserEntity;
import com.example.blog2.service.UserService;


/**
 * @author mxp
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<UserEntity> getUserAreaList() {
        return baseMapper.selectUserAreaList();
    }

}

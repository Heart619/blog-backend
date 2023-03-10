package com.example.blog2.service.impl;

import com.example.blog2.exception.UserStatusException;
import com.example.blog2.interceptor.IPInterceptor;
import com.example.blog2.interceptor.TokenInterceptor;
import com.example.blog2.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.ProjectDao;
import com.example.blog2.entity.ProjectEntity;
import com.example.blog2.service.ProjectService;
import org.springframework.util.StringUtils;


/**
 * @author mxp
 */

@Slf4j
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectDao, ProjectEntity> implements ProjectService {

    @Autowired
    private OSSUtils ossUtils;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();

        String search = (String) params.get("search");
        if (!StringUtils.isEmpty(search)) {
            queryWrapper.like("title", search);
        }

        IPage<ProjectEntity> page = this.page(
                new Query<ProjectEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void removeProject(Long id) {
        if (!TokenUtil.checkRootType()) {
            throw new UserStatusException();
        }

        ProjectEntity project = getById(id);
        if (!DefaultImgUtils.isDefaultBackImg(project.getPicUrl())) {
            ossUtils.del(project.getPicUrl());
        }

        removeById(id);
    }

    @Override
    public void updateProject(ProjectEntity project) {
        if (!TokenUtil.checkRootType()) {
            throw new UserStatusException();
        }

        if (!StringUtils.isEmpty(project.getPicUrl())) {
            ProjectEntity old = getById(project.getId());
            ossUtils.del(old.getPicUrl());
        }

        updateById(project);

    }

    @Override
    public void addProject(ProjectEntity project) {
        if (!TokenUtil.checkRootType()) {
            throw new UserStatusException();
        }

        if (StringUtils.isEmpty(project.getPicUrl())) {
            project.setPicUrl(DefaultImgUtils.getDefaultBackImg());
        }

        save(project);
    }

}

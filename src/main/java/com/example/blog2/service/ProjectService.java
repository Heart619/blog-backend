package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.entity.ProjectEntity;
import com.example.blog2.utils.PageUtils;

import java.util.Map;

/**
 * @author mxp
 * @date 2023-01-25 09:47:20
 */
public interface ProjectService extends IService<ProjectEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 删除项目
     * @param id
     */
    void removeProject(Long id);

    /**
     * 更新项目
     * @param project
     */
    void updateProject(ProjectEntity project);

    /**
     * 新增项目
     * @param project
     */
    void addProject(ProjectEntity project);
}


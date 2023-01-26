package com.example.blog2.service;

import com.example.blog2.po.Project;

import java.util.List;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/12 21:07
 */
public interface ProjectService {
    List<Project> listProject();

    void deleteProject(Long id);

    Project saveProject(Project project);

    Project updateProject(Long id,Project project);
}

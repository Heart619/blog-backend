package com.example.blog2.service.impl;

import com.example.blog2.dao.ProjectRepository;
import com.example.blog2.po.Project;
import com.example.blog2.service.PicturesService;
import com.example.blog2.service.ProjectService;
import com.example.blog2.utils.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/12 21:08
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PicturesService picturesService;

    @Override
    public List<Project> listProject() {
        return projectRepository.findAll();
    }

    @Override
    public void deleteProject(Long id) {
        Project project = projectRepository.getOne(id);
        if (!StringUtils.isEmpty(project.getPic_url())) {
            picturesService.delOssImg(project.getPic_url());
        }
        projectRepository.deleteById(id);
    }

    @Override
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Long id, Project project) {
        Project p = projectRepository.getOne(id);
        BeanUtils.copyProperties(project,p, MyBeanUtils.getNullPropertyNames(project));
        return projectRepository.save(p);
    }
}

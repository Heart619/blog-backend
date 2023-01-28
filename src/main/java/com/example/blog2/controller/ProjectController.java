package com.example.blog2.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.blog2.entity.ProjectEntity;
import com.example.blog2.service.ProjectService;



/**
 *
 *
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@RestController
@RequestMapping("/admin/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		ProjectEntity project = projectService.getById(id);

        return R.ok().put("project", project);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody ProjectEntity project){
        projectService.addProject(project);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody ProjectEntity project){
        projectService.updateProject(project);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/{id}/delete")
    public R delete(@PathVariable("id") Long id){
        projectService.removeProject(id);
        return R.ok();
    }

}

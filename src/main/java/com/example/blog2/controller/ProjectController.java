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
@RequestMapping("project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam(required = false) Map<String, Object> params){
        if (params == null) {
            params = new HashMap<>(1);
        }
        PageUtils page = projectService.queryPage(params);

        return R.ok().put("page", page);
    }


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
        try {
            projectService.addProject(project);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody ProjectEntity project){
        try {
            projectService.updateProject(project);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }

    /**
     * 删除
     */
    @PostMapping("/{id}/delete")
    public R delete(@PathVariable("id") Long id){
        try {
            projectService.removeProject(id);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }

}

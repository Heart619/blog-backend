package com.example.blog2.controller.web;

import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/5 21:10
 */
@RestController
public class TagShowController {

    @Autowired
    private TagService tagService;

    @GetMapping("/getTagList")
    public Result getTagList() {
        return new Result(true, StatusCode.OK, "获取博客标签成功", tagService.list());
    }

    @GetMapping("/getFullTagList")
    public Result getFullTagList() {
        return new Result(true, StatusCode.OK, "获取所有博客标签成功", tagService.list());
    }

}

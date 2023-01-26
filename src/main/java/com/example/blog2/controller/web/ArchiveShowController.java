package com.example.blog2.controller.web;

import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/4/20 9:57
 */
@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archiveBlog")
    public Result<Model> archives(){
        return new Result(true, StatusCode.OK, "查询博客列表成功", blogService.list());
    }

    @GetMapping("/countBlog")
    public Result count(){
        return new Result(true, StatusCode.OK, "查询博客列表成功", blogService.list());
    }
}

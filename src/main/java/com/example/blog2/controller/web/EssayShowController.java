package com.example.blog2.controller.web;

import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.service.old.EssayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/12 16:46
 */
//@RestController
public class EssayShowController {
    @Autowired
    private EssayService essayService;
    @GetMapping("/essays")
    public Result essays() {
        return new Result(true, StatusCode.OK, "获取随笔列表成功", essayService.listEssay());
    }
}

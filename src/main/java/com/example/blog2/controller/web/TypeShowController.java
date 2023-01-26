package com.example.blog2.controller.web;

import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.service.TypeService;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/5 21:10
 */
//@RestController
public class TypeShowController {
//    @Autowired
    private TypeService typeService;

    @GetMapping("/getTypeList")
    public Result getTypeList() {
        return new Result(true, StatusCode.OK, "获取博客分类成功",typeService.getAllType());
    }

    @GetMapping("/getFullTypeList")
    public Result getFullTypeList() {
        return new Result(true, StatusCode.OK, "获取博客全部分类成功",typeService.getAllType());
    }

}

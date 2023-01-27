package com.example.blog2.controller.web;

import com.example.blog2.entity.TypeEntity;
import com.example.blog2.service.TypeService;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author mxp
 * @date 2023/1/27 14:00
 */

@RestController
@RequestMapping("/type")
public class TypeWebController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = typeService.queryPage(params);

        return R.ok().put("page", page);
    }

    @GetMapping("/getAllType")
    public R getAllType() {
        List<TypeEntity> typeEntities = typeService.getAllType();
        return R.ok("获取全部博客分类成功").put("data", typeEntities);
    }
}

package com.example.blog2.controller.web;

import com.example.blog2.service.TagService;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author mxp
 * @date 2023/1/27 14:00
 */

@RestController
@RequestMapping("/tag")
public class TagWebController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tagService.queryPage(params);

        return R.ok().put("page", page);
    }

    @GetMapping("/allTag")
    public R getAllTag(){
        return R.ok().put("data", tagService.getAllTag());
    }
}

package com.example.blog2.controller.web;

import com.example.blog2.service.PicturesService;
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
 * @date 2023/2/6 16:01
 */
@RestController
@RequestMapping("/pictures")
public class PicturesWebController {

    @Autowired
    private PicturesService picturesService;

    @GetMapping("/waterfall")
    public R showList(@RequestParam Map<String, Object> params){
        PageUtils page = picturesService.queryPage(params, false);

        return R.ok().put("page", page);
    }
}

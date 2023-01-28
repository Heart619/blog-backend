package com.example.blog2.controller.web;

import com.example.blog2.service.EssayService;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mxp
 * @date 2023/1/27 13:54
 */

@RestController
@RequestMapping("/essay")
public class EssayWebController {

    @Autowired
    private EssayService essayService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = essayService.queryPage(params);

        return R.ok().put("page", page);
    }
}

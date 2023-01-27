package com.example.blog2.controller.web;

import com.example.blog2.service.EssayService;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/list")
    public R list(@RequestBody(required = false) Map<String, Object> params){
        if (params == null) {
            params = new HashMap<>(0);
        }
        PageUtils page = essayService.queryPage(params);

        return R.ok().put("page", page);
    }
}

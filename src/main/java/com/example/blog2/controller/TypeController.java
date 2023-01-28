package com.example.blog2.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.blog2.utils.OSSUtils;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.example.blog2.entity.TypeEntity;
import com.example.blog2.service.TypeService;



/**
 * @author mxp
 * @date 2023-01-25 09:47:20
 */
@RestController
@RequestMapping("/admin/type")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private OSSUtils ossUtils;

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		TypeEntity type = typeService.getById(id);

        return R.ok().put("type", type);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody TypeEntity type){
        TypeEntity t = typeService.addType(type);
        return R.ok().put("data", t);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody TypeEntity type){
        typeService.updateType(type);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		typeService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @PostMapping("/{id}/delete")
    public R delete(@PathVariable Long id) {
        TypeEntity type = typeService.getById(id);
        typeService.removeById(id);
        if (!StringUtils.isEmpty(type.getPicUrl())) {
            ossUtils.del(type.getPicUrl());
        }
        return R.ok("删除成功");
    }

}

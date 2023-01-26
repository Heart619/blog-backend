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
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@RestController
@RequestMapping("/type")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private OSSUtils ossUtils;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = typeService.queryPage(params);

        return R.ok().put("page", page);
    }


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
        try {
            typeService.addType(type);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }

    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody TypeEntity type){
        try {
            typeService.updateType(type);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }

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
        try {
            TypeEntity type = typeService.getById(id);
            typeService.removeById(id);
            if (!StringUtils.isEmpty(type.getPicUrl())) {
                ossUtils.del(type.getPicUrl());
            }
            return R.ok("删除成功");
        } catch (Exception e) {
            return R.error();
        }
    }

    @GetMapping("/getAllType")
    public R getAllType() {
        List<TypeEntity> typeEntities = typeService.getAllType();
        return R.ok("获取全部博客分类成功").put("data", typeEntities);
    }

}

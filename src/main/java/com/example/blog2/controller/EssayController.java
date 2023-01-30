package com.example.blog2.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import com.example.blog2.vo.EssayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.blog2.entity.EssayEntity;
import com.example.blog2.service.EssayService;



/**
 * @author mxp
 * @date 2023-01-25 09:47:20
 */
@RestController
@RequestMapping("/admin/essay")
public class EssayController {

    @Autowired
    private EssayService essayService;

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		EssayEntity essay = essayService.getById(id);

        return R.ok().put("essay", essay);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody EssayEntity essay){
        essayService.saveEssay(essay);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody EssayEntity essay){
		essayService.updateById(essay);

        return R.ok();
    }

    @PostMapping("/{id}/delete")
    public R delEssay(@PathVariable("id") Long id) {
        essayService.delEssayById(id);
        return R.ok();
    }

    @GetMapping("/default/{id}")
    public R getEssayDefaultInfo(@PathVariable("id") Long id) {
        return R.ok().put("data", essayService.getDefaultInfo(id));
    }
}

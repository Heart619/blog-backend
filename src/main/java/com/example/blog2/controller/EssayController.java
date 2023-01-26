package com.example.blog2.controller;

import java.util.Arrays;
import java.util.Map;

import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.blog2.entity.EssayEntity;
import com.example.blog2.service.EssayService;



/**
 * @author mxp
 * @date 2023-01-25 09:47:20
 */
@RestController
@RequestMapping("essay")
public class EssayController {

    @Autowired
    private EssayService essayService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = essayService.queryPage(params);

        return R.ok().put("page", page);
    }


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
        try {
            essayService.saveEssay(essay);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }

    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody EssayEntity essay){
		essayService.updateById(essay);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		essayService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @GetMapping("/{id}/delete")
    public R delEssay(@PathVariable("id") Long id) {
        essayService.removeById(id);
        return R.ok();
    }
}

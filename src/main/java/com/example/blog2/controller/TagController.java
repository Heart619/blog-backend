package com.example.blog2.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.blog2.entity.TypeEntity;
import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.service.TagService;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.blog2.entity.TagEntity;



/**
 *
 *
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tagService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/allTag")
    public R getAllTag(){
        return R.ok().put("data", tagService.getAllTag());
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		TagEntity tag = tagService.getById(id);

        return R.ok().put("tag", tag);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody TagEntity tag){
        try {
            TagEntity tagEntity = tagService.addTag(tag);
            return R.ok("新增成功");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody TagEntity tag){
        try {
            TagEntity tagEntity = tagService.updateTag(tag);
            return R.ok("修改成功");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        try {
            tagService.removeByIds(Arrays.asList(ids));
            return R.ok("删除成功");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/delete")
    public R delete(@PathVariable Long id) {
        try {
            tagService.removeById(id);
            return R.ok("删除成功");
        } catch (Exception e) {
            return R.error();
        }
    }

}

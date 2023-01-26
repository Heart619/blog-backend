package com.example.blog2.controller.web;

import java.util.Arrays;
import java.util.Map;

import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.blog2.entity.BlogTagsEntity;
import com.example.blog2.service.BlogTagsService;



/**
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@RestController
@RequestMapping("blogtags")
public class BlogTagsController {

    @Autowired
    private BlogTagsService blogTagsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("blog2:blogtags:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = blogTagsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("blog2:blogtags:info")
    public R info(@PathVariable("id") Long id){
		BlogTagsEntity blogTags = blogTagsService.getById(id);

        return R.ok().put("blogTags", blogTags);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("blog2:blogtags:save")
    public R save(@RequestBody BlogTagsEntity blogTags){
		blogTagsService.save(blogTags);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("blog2:blogtags:update")
    public R update(@RequestBody BlogTagsEntity blogTags){
		blogTagsService.updateById(blogTags);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("blog2:blogtags:delete")
    public R delete(@RequestBody Long[] ids){
		blogTagsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

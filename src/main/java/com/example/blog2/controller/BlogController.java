package com.example.blog2.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.example.blog2.entity.TagEntity;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import com.example.blog2.vo.DateCountVo;
import com.example.blog2.vo.DelBlogTagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.blog2.entity.BlogEntity;
import com.example.blog2.service.BlogService;

import javax.servlet.http.HttpServletRequest;


/**
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@RestController
@RequestMapping("/admin/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		BlogEntity blog = blogService.getById(id);

        return R.ok().put("blog", blog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody BlogEntity blog) throws ExecutionException, InterruptedException {
        if (blogService.addBlog(blog)) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody BlogEntity blog){
        blogService.updateBlog(blog);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		blogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @PostMapping("/{id}/delete")
    public R delBlog(@PathVariable("id") Long id) throws ExecutionException, InterruptedException {
        blogService.delBlog(id);
        return R.ok();
    }

    @GetMapping("/default/{blog}")
    public R bolgDefaultInfo(@PathVariable("blog") Long id) {
        BlogEntity blog = blogService.getDefaultBlogInfo(id);
        if (blog == null) {
            return R.error("博客消失了");
        }
        return R.ok().put("data", blog);
    }

    @PostMapping("/updateImg")
    public R updateImg(@RequestBody BlogEntity blog) {
        blogService.updateImg(blog);
        return R.ok();
    }

    @PostMapping("/updateType")
    public R updateType(@RequestBody BlogEntity blog) {
        blogService.updateType(blog);
        return R.ok();
    }

    @GetMapping("/{id}/createTag/{name}")
    public R createTag(@PathVariable("id") Long id, @PathVariable("name") String name) {
        TagEntity tag = blogService.createTag(id, name);
        return R.ok().put("data", tag);
    }

    @PostMapping("/delTag")
    public R delTag(@RequestBody DelBlogTagVo vo) {
        blogService.delTag(vo);
        return R.ok();
    }

    @GetMapping("/search")
    public R search(String query) {
        List<BlogEntity> blogs = blogService.search(query);
        return R.ok().put("data", blogs);
    }

    @GetMapping("/getViewCountByMonth")
    public R getViewCountByMonth() {
        List<DateCountVo> res = blogService.getViewCountByMonth();
        return R.ok().put("data", res);
    }

    @GetMapping("/getBlogCountByMonth")
    public R getBlogCountByMonth() {
        List<DateCountVo> res = blogService.getBlogCountByMonth();
        return R.ok().put("data", res);
    }

    @GetMapping("/getAppreciateCountByMonth")
    public R getAppreciateCountByMonth() {
        List<DateCountVo> res = blogService.getAppreciateCountByMonth();
        return R.ok().put("data", res);
    }

    @GetMapping("/getBlogCount")
    public R getBlogCount() {
        Long count = blogService.getBlogCount();
        return R.ok().put("data", count);
    }

    @GetMapping("/getViewCount")
    public R getViewCount() {
        Long count = blogService.getViewCount();
        return R.ok().put("data", count);
    }

    @GetMapping("/getAppreciateCount")
    public R getAppreciateCount() {
        Long count = blogService.getAppreciateCount();
        return R.ok().put("data", count);
    }

}

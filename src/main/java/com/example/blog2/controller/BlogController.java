package com.example.blog2.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.blog2.entity.BlogEntity;
import com.example.blog2.service.BlogService;


/**
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@RestController
@RequestMapping("blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestBody Map<String, Object> params){
        PageUtils page = blogService.queryPage(params);

        return R.ok().put("page", page);
    }


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
    public R save(@RequestBody BlogEntity blog){
		blogService.save(blog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody BlogEntity blog){
		blogService.updateById(blog);

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

    @GetMapping("/{blog}")
    public R bolgInfo(@PathVariable("blog") Long id) {
        BlogEntity blog = null;
        try {
            blog = blogService.getBlogInfo(id);
            if (blog == null) {
                return R.error("博客消失了");
            }
            return R.ok().put("data", blog);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("网络繁忙，请稍后再试");
        }
    }

    @GetMapping("/search")
    public R search(String query) {
        List<BlogEntity> blogs = blogService.search(query);
        return R.ok().put("data", blogs);
    }


    @GetMapping("/getRecommendBlogList")
    public R getRecommedBlog() {
        List<BlogEntity> list = blogService.getRecommedBlog();
        return R.ok().put("data", list);
    }

    @GetMapping("/newblog")
    public R newblog() {
        List<BlogEntity> list = blogService.getNewBlog();
        return R.ok().put("data", list);
    }

    @GetMapping("/getViewCountByMonth")
    public R getViewCountByMonth() {
        List<String> res = blogService.getViewCountByMonth();
        return R.ok().put("data", res);
    }

    @GetMapping("/getBlogCountByMonth")
    public R getBlogCountByMonth() {
        List<String> res = blogService.getBlogCountByMonth();
        return R.ok().put("data", res);
    }

    @GetMapping("/getAppreciateCountByMonth")
    public R getAppreciateCountByMonth() {
        List<String> res = blogService.getAppreciateCountByMonth();
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

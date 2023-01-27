package com.example.blog2.controller.web;

import com.example.blog2.entity.BlogEntity;
import com.example.blog2.service.BlogService;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mxp
 * @date 2023/1/27 13:49
 */

@RestController
@RequestMapping("/blog")
public class BlogWebController {

    @Autowired
    private BlogService blogService;

    @RequestMapping("/list")
    public R list(@RequestBody(required = false) Map<String, Object> params){
        if (params == null) {
            params = new HashMap<>(0);
        }
        PageUtils page = blogService.queryPage(params);

        return R.ok().put("page", page);
    }

    @GetMapping("/{blog}")
    public R bolgInfo(@PathVariable("blog") Long id) {
        BlogEntity blog;
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

}

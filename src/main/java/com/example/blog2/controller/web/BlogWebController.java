package com.example.blog2.controller.web;

import com.example.blog2.entity.BlogEntity;
import com.example.blog2.service.BlogService;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author mxp
 * @date 2023/1/27 13:49
 */

@RestController
@RequestMapping("/blog")
public class BlogWebController {

    @Autowired
    private BlogService blogService;

    @PostMapping("/list")
    public R list(@RequestBody Map<String, Object> params){
        PageUtils page = blogService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/{blog}")
    public R bolgInfo(@PathVariable("blog") Long id) throws ExecutionException, InterruptedException {
        BlogEntity blog = blogService.getBlogInfo(id);
        if (blog == null) {
            return R.error("博客消失了");
        }
        return R.ok().put("data", blog);
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

    @GetMapping("/search")
    public R search(String query) {
        List<BlogEntity> blogs = blogService.search(query);
        return R.ok().put("data", blogs);
    }
}

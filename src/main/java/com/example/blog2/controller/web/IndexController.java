package com.example.blog2.controller.web;

import com.example.blog2.entity.TagEntity;
import com.example.blog2.entity.TypeEntity;
import com.example.blog2.po.*;
import com.example.blog2.service.old.BlogService;
import com.example.blog2.service.TagService;
import com.example.blog2.service.TypeService;
import com.example.blog2.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/3/30 17:52
 */
//@RestController
public class IndexController {

//    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public Result getBlogList(@RequestHeader Map<String,Object>para, @RequestParam String query, @RequestParam String pagenum, @RequestParam String pagesize) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(Integer.parseInt(pagenum)-1, Integer.parseInt(pagesize),sort);
        return new Result(true, StatusCode.OK, "获取博客列表成功", blogService.listBlog(pageable));
    }

    @GetMapping("/getRecommendBlogList")
    public Result getRecommendBlogList() {
        return new Result(true, StatusCode.OK, "获取推荐博客成功", blogService.listRecommendBlogTop(8));
    }

    @GetMapping("/search")
    public Result search(@PageableDefault(size = 8,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query){
        return new Result(true,StatusCode.OK,"获取搜索博客成功",blogService.listBlog("%"+query+"%",pageable));
    }

    @GetMapping("/simple/blog/{id}")
    public Result simpleBlog(@PathVariable Long id) {
        return new Result(true, StatusCode.OK, "获取博客成功", blogService.getSimpleBlog(id));
    }

    @GetMapping("/blog/{id}")
    public Result blog(@PathVariable Long id) {
        return new Result(true, StatusCode.OK, "获取博客成功", blogService.getAndConvert(id));
    }

    @GetMapping("/footer/newblog")
    public Result newblogs(){
        return new Result(true, StatusCode.OK, "获取推荐博客成功", blogService.listRecommendBlogTop(3));
    }

    @GetMapping("/types/{id}")
    public Result types(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id){
        List<TypeEntity> types = typeService.getAllType();
        if (id == -1 && types.size()>0){
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        return new Result(true,StatusCode.OK,"获取分类博客列表成功",blogService.listBlog(pageable,blogQuery));
    }

    @GetMapping("tags/{id}")
    public Result tags(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                       @PathVariable Long id){
        List<TagEntity> tags = tagService.list();
        if (id == -1 && tags.size()>0 ){
            id = tags.get(0).getId();
        }
        return new Result(true,StatusCode.OK,"获取标签博客列表成功",blogService.listBlog(id,pageable));
    }

}

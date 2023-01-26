package com.example.blog2.service.old;

import com.example.blog2.po.Blog;
import com.example.blog2.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/4/13 23:00
 */
public interface BlogService {
//    根据id查询
    Blog getBlog(Long id);
//    分页查询
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Long tagId,Pageable pageable);

    Page<Blog> listBlog(String query,Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);

    Blog getAndConvert(Long id);

    Blog getSimpleBlog(Long id);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();

    Long countViews();

    List<String> ViewCountByMonth();

    List<String> BlogCountByMonth();

    List<String> appreciateCountByMonth();

    Long countAppreciate();

    Long countComment();

}

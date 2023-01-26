package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.entity.BlogEntity;
import com.example.blog2.utils.R;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 *
 *
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
public interface BlogService extends IService<BlogEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取推荐博客
     * @return
     */
    List<BlogEntity> getRecommedBlog();

    /**
     * 获取最新博客
     * @return
     */
    List<BlogEntity> getNewBlog();

    /**
     * 根据月份统计阅读量
     * @return
     */
    List<String> getViewCountByMonth();

    /**
     * 根据月份统计文章数
     * @return
     */
    List<String> getBlogCountByMonth();

    /**
     * 根据月份统计点赞数
     * @return
     */
    List<String> getAppreciateCountByMonth();

    /**
     * 获得文章总数
     * @return
     */
    Long getBlogCount();

    /**
     * 获得总阅读量
     * @return
     */
    Long getViewCount();

    /**
     * 获得总点赞数
     * @return
     */
    Long getAppreciateCount();

    /**
     * 查询博客详情
     * @param id
     * @return
     */
    BlogEntity getBlogInfo(Long id) throws ExecutionException, InterruptedException;

    /**
     * 查询
     * @param query
     * @return
     */
    List<BlogEntity> search(String query);
}


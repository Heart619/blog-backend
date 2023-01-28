package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.entity.TagEntity;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.entity.BlogEntity;
import com.example.blog2.utils.R;
import com.example.blog2.vo.DateCountVo;
import com.example.blog2.vo.DelBlogTagVo;

import javax.servlet.http.HttpServletRequest;
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
    List<DateCountVo> getViewCountByMonth();

    /**
     * 根据月份统计文章数
     * @return
     */
    List<DateCountVo> getBlogCountByMonth();

    /**
     * 根据月份统计点赞数
     * @return
     */
    List<DateCountVo> getAppreciateCountByMonth();

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

    /**
     * 删除博客
     * @param id
     */
    void delBlog(Long id) throws ExecutionException, InterruptedException;

    /**
     * 修改博客首图
     * @param blog
     */
    void updateImg(BlogEntity blog);

    /**
     * 获得博客默认信息
     * @param id
     * @return
     */
    BlogEntity getDefaultBlogInfo(Long id);

    /**
     * 更新博客内容
     * @param blog
     */
    void updateBlog(BlogEntity blog);

    /**
     * 新增博客
     * @param blog
     * @return
     */
    boolean addBlog(BlogEntity blog) throws ExecutionException, InterruptedException;

    /**
     * 修改博客类型
     * @param blog
     */
    void updateType(BlogEntity blog);

    /**
     * 删除博客标签
     * @param vo
     */
    void delTag(DelBlogTagVo vo);

    /**
     * 创建标签
     * @param id
     * @param name
     * @return
     */
    TagEntity createTag(Long id, String name);
}


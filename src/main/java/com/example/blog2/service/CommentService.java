package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.entity.CommentEntity;
import com.example.blog2.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author mxp
 * @date 2023-01-25 09:47:20
 */
public interface CommentService extends IService<CommentEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取评论集合
     * @param blogId
     * @return
     */
    List<CommentEntity> getCommentsByBlogId(Long blogId);

    /**
     * 删除评论
     * @param id
     */
    void delComment(Long id);

    /**
     * 获取最新评论
     * @return
     */
    List<CommentEntity> getNewComments();

    /**
     * 根据月份统计评论数
     * @return
     */
    List<String> getCommentCountByMonth();

    /**
     * 统计总评论数
     * @return
     */
    Long getCommentCount();

    /**
     * 获取文章评论
     * @param id
     * @return
     */
    List<CommentEntity> getBlogComments(Long id);
}


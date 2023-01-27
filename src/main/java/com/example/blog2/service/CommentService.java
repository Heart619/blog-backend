package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.entity.CommentEntity;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.vo.BlogCommentVo;

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
     * 用户删除时更新评论信息
     * @param id
     */
    void userDelUpdateComment(Long id);

    /**
     * 分页查询博客评论
     * @param params
     * @return
     */
    PageUtils queryBlogCommentPage(Map<String, Object> params);

    /**
     * 显示该父评论的子评论
     * @param id
     * @param cid
     * @return
     */
    List<BlogCommentVo> getCmtByPmt(Long id, Long cid);

    /**
     * 评论
     * @param comment
     * @return
     */
    BlogCommentVo addComment(CommentEntity comment);
}


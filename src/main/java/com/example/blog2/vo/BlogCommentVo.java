package com.example.blog2.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author mxp
 * @date 2023/1/27 20:26
 */
@Data
public class BlogCommentVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 头像url
     */
    private String avatar;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 父评论id
     */
    private Long parentCommentId;
    /**
     * 评论所属文章id
     */
    private Long blogId;
    /**
     * 是否为作者评论
     */
    private Boolean adminComment;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论时间
     */
    private Date createTime;

    private List<BlogCommentVo> children;

    private BlogCommentVo parentComment;

    /**
     * 是否展开子评论
     */
    private Boolean showChildren;
}

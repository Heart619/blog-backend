package com.example.blog2.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@Data
@TableName("t_comment")
public class CommentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
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
	 * 评论所属文章id
	 */
	private Long blogId;
	/**
	 * 父评论id
	 */
	private Long parentCommentId;
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

	@TableField(exist = false)
	private String blogTitle;

}

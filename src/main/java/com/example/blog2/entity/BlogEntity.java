package com.example.blog2.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 *
 *
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@Data
@TableName("t_blog")
public class BlogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 *
	 */
	private Long appreciation;
	/**
	 *
	 */
	private Boolean commentabled;
	/**
	 *
	 */
	private String content;
	/**
	 *
	 */
	private Date createTime;
	/**
	 *
	 */
	private String description;
	/**
	 *
	 */
	private String firstPicture;
	/**
	 *
	 */
	private String flag;
	/**
	 *
	 */
	private Boolean published;
	/**
	 *
	 */
	private Boolean recommend;
	/**
	 *
	 */
	private Boolean shareStatement;
	/**
	 *
	 */
	private String title;
	/**
	 *
	 */
	private Date updateTime;
	/**
	 *
	 */
	private Integer views;
	/**
	 *
	 */
	private Long typeId;
	/**
	 *
	 */
	private Long userId;

	@TableField(exist = false)
	private String userAvatar;

	@TableField(exist = false)
	private String userNickName;

	@TableField(exist = false)
	private String typeName;

	@TableField(exist = false)
	private List<TagEntity> tags;

	@TableField(exist = false)
	private String tagIds;
}

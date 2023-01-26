package com.example.blog2.entity;

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
@TableName("t_project")
public class ProjectEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 项目标题
	 */
	private String title;
	/**
	 * 
	 */
	private String techs;
	/**
	 * 项目图片
	 */
	private String picUrl;
	/**
	 * 项目地址
	 */
	private String url;
	/**
	 * 
	 */
	private String content;
	/**
	 * 
	 */
	private Integer type;

}

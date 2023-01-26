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
@TableName("t_type")
public class TypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 类型id
	 */
	@TableId
	private Long id;
	/**
	 * 类型名称
	 */
	private String name;
	/**
	 * 颜色
	 */
	private String color;
	/**
	 * 类型图片url
	 */
	private String picUrl;

	@TableField(exist = false)
	private Integer blogNum;

}

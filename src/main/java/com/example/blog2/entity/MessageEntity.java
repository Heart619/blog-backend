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
@TableName("t_message")
public class MessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private String nickname;
	/**
	 * 
	 */
	private String avatar;
	/**
	 * 
	 */
	private String content;
	/**
	 * 
	 */
	private Long userId;
	/**
	 * 
	 */
	private Date createTime;

}

package com.example.blog2.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 *
 *
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@Data
@TableName("t_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 *
	 */
	private String avatar;
	/**
	 *
	 */
	private Date createTime;
	/**
	 *
	 */
	private String email;
	/**
	 *
	 */
	private String nickname;
	/**
	 *
	 */
	@JsonIgnore
	private String password;
	/**
	 *
	 */
	private Integer type;
	/**
	 *
	 */
	private Date updateTime;
	/**
	 *
	 */
	private String username;
	/**
	 *
	 */
	private Date lastLoginTime;
	/**
	 *
	 */
	private String loginProvince;
	/**
	 *
	 */
	private String loginCity;
	/**
	 *
	 */
	@JsonIgnore
	private Double loginLat;
	/**
	 *
	 */
	@JsonIgnore
	private Double loginLng;

}

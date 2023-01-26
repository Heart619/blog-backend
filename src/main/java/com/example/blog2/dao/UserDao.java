package com.example.blog2.dao;

import com.example.blog2.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 *
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

    /**
     * 查询所有用户的头像和昵称
     * @return
     */
    List<UserEntity> selectUserAvatarAndNickName();

    /**
     * 获得所有用户省份信息
     * @return
     */
    List<UserEntity> selectUserAreaList();
}

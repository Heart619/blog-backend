package com.example.blog2.dao;

import com.example.blog2.entity.MessageEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 *
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@Mapper
public interface MessageDao extends BaseMapper<MessageEntity> {

    /**
     * 删除用户时更新留言
     * @param id
     * @param defaultAvatar
     */
    void updateUserMessage(@Param("id") Long id, @Param("defaultAvatar") String defaultAvatar);

    /**
     * 更新留言信息中的用户昵称
     * @param id
     * @param nickname
     */
    void updateMessageForUserUpdate(@Param("id") Long id, @Param("nickname") String nickname);
}

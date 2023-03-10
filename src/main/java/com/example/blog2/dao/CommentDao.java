package com.example.blog2.dao;

import com.example.blog2.entity.CommentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blog2.vo.DateCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@Mapper
public interface CommentDao extends BaseMapper<CommentEntity> {

    /**
     * 得到所有子评论
     * @param faId
     * @return
     */
    List<Long> selectChilderComment(@Param("faId") Long faId);

    /**
     * 获取最新评论
     * @return
     */
    List<CommentEntity> selectNewComments();

    /**
     * 根据月份统计评论数
     * @return
     */
    List<DateCountVo> selectCommentCountByMonth();

    /**
     * 统计总评论数
     * @return
     */
    Long selectCommentCount();

    /**
     * 用户删除时更新评论信息
     * @param id
     * @param defaultAvatar
     */
    void userDelUpdateComment(@Param("id") Long id, @Param("defaultAvatar") String defaultAvatar);

    /**
     * 更新评论信息中的用户昵称
     * @param id
     * @param nickname
     */
    void updateCommentForUserUpdate(@Param("id") Long id, @Param("nickname") String nickname);
}

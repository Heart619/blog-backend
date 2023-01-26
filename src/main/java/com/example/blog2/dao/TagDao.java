package com.example.blog2.dao;

import com.example.blog2.entity.TagEntity;
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
public interface TagDao extends BaseMapper<TagEntity> {

    /**
     * 查询所有博客与标签关联关系
     * @return
     */
    List<Long> selectAllBlogTagId();
}

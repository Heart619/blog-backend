package com.example.blog2.dao;

import com.example.blog2.entity.PicturesEntity;
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
public interface PicturesDao extends BaseMapper<PicturesEntity> {

    /**
     * 修改图片所属博客
     * @param lid
     * @param blogId
     * @param type
     */
    void updateOldPicturesByLongId(@Param("lid") Long lid, @Param("blogId") Long blogId, @Param("type") Integer type);

    /**
     * 更新照片可见状态
     * @param pictures
     */
    void updatePicTypeById(PicturesEntity pictures);
}

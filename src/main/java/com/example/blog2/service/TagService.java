package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.entity.TagEntity;
import com.example.blog2.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author mxp
 * @date 2023-01-25 09:47:20
 */
public interface TagService extends IService<TagEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获得所有标签及标签所属文章
     * @return
     */
    List<TagEntity> getAllTag();

    /**
     * 新增标签
     * @param tag
     * @return
     */
    TagEntity addTag(TagEntity tag);

    /**
     * 更新标签
     * @param tag
     * @return
     */
    TagEntity updateTag(TagEntity tag);
}


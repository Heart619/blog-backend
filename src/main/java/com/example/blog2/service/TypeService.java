package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.entity.TagEntity;
import com.example.blog2.entity.TypeEntity;
import com.example.blog2.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
public interface TypeService extends IService<TypeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取全部分类
     * @return
     */
    List<TypeEntity> getAllType();

    /**
     * 新增分类
     * @param tag
     * @return
     */
    TypeEntity addType(TypeEntity tag);

    /**
     * 更新分类
     * @param tag
     * @return
     */
    TypeEntity updateType(TypeEntity tag);
}


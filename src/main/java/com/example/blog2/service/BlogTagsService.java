package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.entity.BlogTagsEntity;
import com.example.blog2.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
public interface BlogTagsService extends IService<BlogTagsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 得到博客对应标签
     * @return
     */
    List<String> getBlogToTag();
}


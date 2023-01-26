package com.example.blog2.service.impl;

import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.BlogTagsDao;
import com.example.blog2.entity.BlogTagsEntity;
import com.example.blog2.service.BlogTagsService;


@Service("blogTagsService")
public class BlogTagsServiceImpl extends ServiceImpl<BlogTagsDao, BlogTagsEntity> implements BlogTagsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BlogTagsEntity> page = this.page(
                new Query<BlogTagsEntity>().getPage(params),
                new QueryWrapper<BlogTagsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<String> getBlogToTag() {
        return baseMapper.selectBlogToTag();
    }

}

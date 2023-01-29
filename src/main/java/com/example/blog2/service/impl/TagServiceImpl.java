package com.example.blog2.service.impl;

import com.example.blog2.entity.BlogEntity;
import com.example.blog2.entity.BlogTagsEntity;
import com.example.blog2.interceptor.IPInterceptor;
import com.example.blog2.service.BlogTagsService;
import com.example.blog2.service.TagService;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.TagDao;
import com.example.blog2.entity.TagEntity;

/**
 *
 * @author mxp
 */

@Slf4j
@Service
public class TagServiceImpl extends ServiceImpl<TagDao, TagEntity> implements TagService {

    @Autowired
    private BlogTagsService blogTagsService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TagEntity> page = this.page(
                new Query<TagEntity>().getPage(params),
                new QueryWrapper<>()
        );
        List<TagEntity> records = page.getRecords();
        setBlogNum(records);

//        log.info("IP：{}， 分页查询标签", IPInterceptor.IP_INFO.get());
        return new PageUtils(page);
    }

    @Override
    public List<TagEntity> getAllTag() {
        List<TagEntity> res = list();
        setBlogNum(res);

//        log.info("IP：{}， 查询所有标签信息", IPInterceptor.IP_INFO.get());
        return res;
    }

    @Override
    public TagEntity addTag(TagEntity tag) {
        if (existsName(tag.getName())) {
            throw new RuntimeException("标签名称重复");
        }
        save(tag);

        log.info("IP：{}， 新增标签[{}]", IPInterceptor.IP_INFO.get(), tag.getName());
        return tag;
    }

    @Override
    public TagEntity updateTag(TagEntity tag) {
        if (existsName(tag.getName())) {
            throw new RuntimeException("标签名称重复");
        }
        updateById(tag);

        log.info("IP：{}， 更新标签", IPInterceptor.IP_INFO.get());
        return tag;
    }

    @Override
    public boolean delTag(Long id) {
        BlogTagsEntity entity = blogTagsService.getOne(new QueryWrapper<BlogTagsEntity>().eq("tags_id", id).last("limit 1"));
        if (entity != null) {
            return false;
        }
        removeById(id);

        log.info("IP：{}， 删除标签", IPInterceptor.IP_INFO.get());
        return true;
    }

    private void setBlogNum(List<TagEntity> list) {
        List<Long> ids = baseMapper.selectAllBlogTagId();
        Map<Long, Integer> map = new HashMap<>(list.size());
        ids.forEach(x -> map.put(x, map.getOrDefault(x, 0) + 1));
        list.forEach(x -> x.setBlogNum(map.getOrDefault(x.getId(), 0)));
    }

    private boolean existsName(String name) {
        TagEntity entity = getOne(new QueryWrapper<TagEntity>().eq("name", name).last("limit 1"));
        return entity != null;
    }

}

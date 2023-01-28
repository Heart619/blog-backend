package com.example.blog2.service.impl;

import com.example.blog2.constant.ConstantImg;
import com.example.blog2.dao.BlogDao;
import com.example.blog2.entity.BlogEntity;
import com.example.blog2.entity.TagEntity;
import com.example.blog2.service.TypeService;
import com.example.blog2.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.TypeDao;
import com.example.blog2.entity.TypeEntity;
import org.springframework.util.StringUtils;


/**
 * @author mxp
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeDao, TypeEntity> implements TypeService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private OSSUtils ossUtils;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<TypeEntity> queryWrapper = new QueryWrapper<>();
        String search = (String) params.get("search");
        if (!StringUtils.isEmpty(search)) {
            queryWrapper.like("name", search);
        }

        IPage<TypeEntity> page = this.page(
                new Query<TypeEntity>().getPage(params),
                queryWrapper
        );
        setBlogNum(page.getRecords());
        return new PageUtils(page);
    }

    @Override
    public List<TypeEntity> getAllType() {
        List<TypeEntity> list = list();
        setBlogNum(list);
        return list;
    }

    @Override
    public TypeEntity addType(TypeEntity typeEntity) {
        if (existsName(typeEntity.getName())) {
            throw new RuntimeException("分类名称重复");
        }

        if (StringUtils.isEmpty(typeEntity.getPicUrl())) {
            typeEntity.setPicUrl(DefaultImgUtils.getDefaultBackImg());
        }
        save(typeEntity);
        return typeEntity;
    }

    @Override
    public TypeEntity updateType(TypeEntity type) {
        if (existsName(type.getName())) {
            throw new RuntimeException("分类名称重复");
        }
        updateById(type);
        return type;
    }

    @Override
    public boolean delType(Long id) {
        BlogEntity blog = blogDao.selectOne(new QueryWrapper<BlogEntity>().eq("type_id", id));
        if (blog != null) {
            return false;
        }

        TypeEntity type = getById(id);
        if (!DefaultImgUtils.isDefaultBackImg(type.getPicUrl())) {
            ossUtils.del(type.getPicUrl());
        }
        removeById(id);

        return true;
    }

    private void setBlogNum(List<TypeEntity> list) {
        List<Long> ids = blogDao.selectAllBlogTypeId();
        Map<Long, Integer> map = new HashMap<>(list.size());
        ids.forEach(x -> map.put(x, map.getOrDefault(x, 0) + 1));
        list.forEach(x -> x.setBlogNum(map.getOrDefault(x.getId(), 0)));
    }

    private boolean existsName(String name) {
        TypeEntity entity = getOne(new QueryWrapper<TypeEntity>().eq("name", name));
        return entity != null;
    }



}

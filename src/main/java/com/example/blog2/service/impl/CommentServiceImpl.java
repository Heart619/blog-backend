package com.example.blog2.service.impl;

import com.example.blog2.dao.BlogDao;
import com.example.blog2.entity.BlogEntity;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.CommentDao;
import com.example.blog2.entity.CommentEntity;
import com.example.blog2.service.CommentService;
import org.springframework.util.CollectionUtils;


/**
 * @author mxp
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, CommentEntity> implements CommentService {

    @Autowired
    private BlogDao blogDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CommentEntity> page = this.page(
                new Query<CommentEntity>().getPage(params),
                new QueryWrapper<CommentEntity>()
        );

        List<CommentEntity> records = page.getRecords();
        Map<Long, String> map = blogDao.selectAllBlogIdAndTitles().stream().collect(Collectors.toMap(BlogEntity::getId, BlogEntity::getTitle));

        records.forEach(x -> {
            x.setBlogTitle(map.getOrDefault(x.getBlogId(), "暂无......"));
        });
        page.setRecords(records);
        return new PageUtils(page);
    }

    @Override
    public List<CommentEntity> getCommentsByBlogId(Long blogId) {
        return list(new QueryWrapper<CommentEntity>().eq("blog_id", blogId).orderByDesc("create_time"));
    }

    @Override
    public void delComment(Long id) {
        delChildrenComment(id);
        removeById(id);
    }

    @Override
    public List<CommentEntity> getNewComments() {
        return baseMapper.selectNewComments();
    }

    @Override
    public List<String> getCommentCountByMonth() {
        return baseMapper.selectCommentCountByMonth();
    }

    @Override
    public Long getCommentCount() {
        return baseMapper.selectCommentCount();
    }

    private void delChildrenComment(Long faId) {
        List<Long> childes = baseMapper.selectChilderComment(faId);
        if (CollectionUtils.isEmpty(childes)) {
            return;
        }
        childes.forEach(this::delChildrenComment);
        removeByIds(childes);
    }

}

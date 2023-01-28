package com.example.blog2.service.impl;

import com.example.blog2.constant.ConstantImg;
import com.example.blog2.dao.BlogDao;
import com.example.blog2.entity.BlogEntity;
import com.example.blog2.utils.DefaultImgUtils;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.Query;
import com.example.blog2.vo.BlogCommentVo;
import com.example.blog2.vo.DateCountVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
                new QueryWrapper<CommentEntity>().orderByDesc("create_time")
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
    public List<DateCountVo> getCommentCountByMonth() {
        return baseMapper.selectCommentCountByMonth();
    }

    @Override
    public Long getCommentCount() {
        return baseMapper.selectCommentCount();
    }

    @Override
    public void userDelUpdateComment(Long id) {
        this.baseMapper.userDelUpdateComment(id, DefaultImgUtils.getDefaultAvatarImg());
    }

    @Override
    public PageUtils queryBlogCommentPage(Map<String, Object> params) {
        long blog = Long.parseLong(String.valueOf(params.get("blog")));
        IPage<CommentEntity> page = this.page(
                new Query<CommentEntity>().getPage(params),
                new QueryWrapper<CommentEntity>().eq("blog_id", blog).eq("parent_comment_id", -1).orderByDesc("create_time")
        );

        PageUtils pageUtils = new PageUtils(page);
        List<BlogCommentVo> blogCommentVos = pageUtils.getList().stream().map(x -> {
            BlogCommentVo vo = new BlogCommentVo();
            BeanUtils.copyProperties(x, vo);
            vo.setChildren(Collections.emptyList());
            vo.setParentComment(null);
            vo.setShowChildren(false);
            return vo;
        }).collect(Collectors.toList());

        pageUtils.setList(blogCommentVos);
        return pageUtils;
    }

    @Override
    public List<BlogCommentVo> getCmtByPmt(Long blog, Long cid) {
       return list(new QueryWrapper<CommentEntity>().eq("blog_id", blog).ne("parent_comment_id", -1).orderByDesc("create_time")).stream().map(x -> {
            BlogCommentVo vo = new BlogCommentVo();
            BeanUtils.copyProperties(x, vo);
            vo.setChildren(Collections.emptyList());
            vo.setParentComment(null);
            vo.setShowChildren(false);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public BlogCommentVo addComment(CommentEntity comment) {
        BlogCommentVo commentVo = new BlogCommentVo();
        save(comment);
        BeanUtils.copyProperties(comment, commentVo);
        commentVo.setShowChildren(false);
        commentVo.setParentComment(null);
        commentVo.setChildren(Collections.emptyList());
        return commentVo;
    }

    @Override
    public void updateCommentForUserUpdate(Long id, String nickname) {
        baseMapper.updateCommentForUserUpdate(id, nickname);
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

package com.example.blog2.service.impl;

import com.example.blog2.dao.UserDao;
import com.example.blog2.entity.*;
import com.example.blog2.service.*;
import com.example.blog2.utils.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.BlogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * @author mxp
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogDao, BlogEntity> implements BlogService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogTagsService blogTagsService;

    @Autowired
    private UserService userService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private OSSUtils ossUtils;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<BlogEntity> queryWrapper = new QueryWrapper<>();
        String query = String.valueOf(params.get("query"));
        if (!StringUtils.isEmpty(query) && !"null".equals(query)) {
            queryWrapper.like("title", query);
        }

        Object typeId1 = params.get("typeId");
        if (typeId1 != null) {
            Long typeId = Long.valueOf(String.valueOf(typeId1));
            if (!typeId.equals(-1L)) {
                queryWrapper.eq("type_id", typeId);
            }
        }

        IPage<BlogEntity> page = this.page(
                new Query<BlogEntity>().getPage(params),
                queryWrapper
        );

        CompletableFuture<Void> tagFuture = CompletableFuture.runAsync(() -> {
            Object tagId1 = params.get("tagId");
            if (tagId1 != null) {
                Long tagId = Long.valueOf(String.valueOf(tagId1));
                if (!tagId.equals(-1L)) {
                    List<BlogTagsEntity> entityList = blogTagsService.list(new QueryWrapper<BlogTagsEntity>().eq("tags_id", tagId));
                    Set<Long> set = entityList.stream().map(BlogTagsEntity::getBlogsId).collect(Collectors.toSet());
                    List<BlogEntity> collect = page.getRecords().stream().filter(x -> set.contains(x.getId())).collect(Collectors.toList());
                    page.setRecords(collect);
                }
            }
        }, executor);
        List<BlogEntity> records = page.getRecords();

        CompletableFuture<Void> userFuture = tagFuture.thenRunAsync(() -> {
            List<UserEntity> userEntities = userDao.selectUserAvatarAndNickName();
            Map<Long, TypeEntity> typeEntityMap = typeService.list().stream().collect(Collectors.toMap(TypeEntity::getId, (x) -> x));
            Map<Long, UserEntity> map = userEntities.stream().collect(Collectors.toMap(UserEntity::getId, (x) -> x));
            records.forEach(x -> {
                UserEntity user = map.get(x.getUserId());
                TypeEntity type = typeEntityMap.get(x.getTypeId());
                x.setUserAvatar(user == null ? "default/avatar.png" : user.getAvatar());
                x.setUserNickName(user == null ? "数据异常" : user.getNickname());
                x.setTypeName(type == null ? "" : type.getName());
            });
        }, executor);

        CompletableFuture<Void> tagsFuture = tagFuture.thenRunAsync(() -> {
            Object needTags = params.get("needTags");
            if (needTags != null) {
                List<TagEntity> list = tagService.list();
                Map<Long, TagEntity> tagEntityMap = list.stream().collect(Collectors.toMap(TagEntity::getId, x -> x));
                List<String> strs = blogTagsService.getBlogToTag();
                Map<Long, String> collect = strs.stream().collect(Collectors.toMap(x -> {
                    String[] s = x.split("_");
                    return Long.parseLong(s[0]);
                }, x -> x.split("_")[1]));

                records.forEach(x -> {
                    String s = collect.get(x.getId());
                    if (!StringUtils.isEmpty(s)) {
                        String[] ids = s.split(",");
                        List<TagEntity> tagEntities = new ArrayList<>();
                        for (String id : ids) {
                            TagEntity tag = tagEntityMap.get(Long.parseLong(id));
                            if (tag != null) {
                                tagEntities.add(tag);
                            }
                        }
                        x.setTags(tagEntities);
                    }
                });
            }
        }, executor);


        try {
            CompletableFuture.allOf(userFuture, tagsFuture).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return new PageUtils(page);
    }

    @Override
    public List<BlogEntity> getRecommedBlog() {
        return baseMapper.selectRecommedBlog();
    }

    @Override
    public List<BlogEntity> getNewBlog() {
        return baseMapper.selectNewBlog();
    }

    @Override
    public List<String> getViewCountByMonth() {
        return baseMapper.selectViewCountByMonth();
    }

    @Override
    public List<String> getBlogCountByMonth() {
        return baseMapper.selectBlogCountByMonth();
    }

    @Override
    public List<String> getAppreciateCountByMonth() {
        return baseMapper.selectAppreciateCountByMonth();
    }

    @Override
    public Long getBlogCount() {
        return baseMapper.selectBlogCount();
    }

    @Override
    public Long getViewCount() {
        return baseMapper.selectViewCount();
    }

    @Override
    public Long getAppreciateCount() {
        return baseMapper.selectAppreciateCount();
    }

    @Override
    public BlogEntity getBlogInfo(Long id) throws ExecutionException, InterruptedException {
        BlogEntity blog = getById(id);
        if (blog == null) {
            return null;
        }

        CompletableFuture<Void> tag = CompletableFuture.runAsync(() -> {
            // 设置标签
            blog.setTags(getBlogTags(id));
        }, executor);

        CompletableFuture<Void> type = CompletableFuture.runAsync(() -> {
            // 获取类型信息
            blog.setTypeName(getBlogTypeInfo(blog.getTypeId()));
        }, executor);

        CompletableFuture<Void> author = CompletableFuture.runAsync(() -> {
            // 获取作者信息
            UserEntity user = getBlogAuthor(blog.getUserId());
            blog.setUserAvatar(user.getAvatar());
            blog.setUserNickName(user.getNickname());
        }, executor);

        CompletableFuture<Void> content = CompletableFuture.runAsync(() -> {
            // 获取正文
            String text = getText(blog.getContent());
            blog.setContent(text);
        }, executor);

        CompletableFuture.allOf(tag, type, content, author).get();
        return blog;
    }

    @Override
    public List<BlogEntity> search(String query) {
        return baseMapper.selectCondition(query);
    }

    private UserEntity getBlogAuthor(Long userId) {
        return userService.getById(userId);
    }

    private String getBlogTypeInfo(Long id) {
        TypeEntity type = typeService.getById(id);
        return type.getName();
    }

    private List<TagEntity> getBlogTags(Long id) {
        List<BlogTagsEntity> blogTagsEntityList = blogTagsService.list(new QueryWrapper<BlogTagsEntity>().eq("blogs_id", id));
        return blogTagsEntityList.stream().map(x -> tagService.getById(x.getTagsId())).collect(Collectors.toList());
    }

    private String getText(String content) {
        byte[] bytes = ossUtils.loadText(content);
        return MarkdownUtils.markdownToHtmlExtensions(new String(bytes, StandardCharsets.UTF_8));
    }

}

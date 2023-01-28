package com.example.blog2.service.impl;

import com.example.blog2.config.OSSConfig;
import com.example.blog2.constant.ConstantImg;
import com.example.blog2.dao.UserDao;
import com.example.blog2.entity.*;
import com.example.blog2.service.*;
import com.example.blog2.utils.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.BlogDao;
import com.example.blog2.vo.DateCountVo;
import com.example.blog2.vo.DelBlogTagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 * @author mxp
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogDao, BlogEntity> implements BlogService {

    @Autowired
    private TransactionDefinition transactionDefinition;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

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
    private CommentService commentService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private OSSUtils ossUtils;

    @Autowired
    private OSSConfig ossConfig;

    @Autowired
    private PicturesService picturesService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<BlogEntity> queryWrapper = new QueryWrapper<>();
        String query = String.valueOf(params.get("title"));
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

        Object userId = params.get("userId");
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        queryWrapper.orderByDesc("create_time", "views");
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
                if (user != null) {
                    x.setUserAvatar(user.getAvatar());
                    x.setUserNickName(user.getNickname());
                } else {
                    x.setUserAvatar(ConstantImg.DEFAULT_AVATAR);
                    x.setUserNickName("用户已注销");
                }
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
    public List<DateCountVo> getViewCountByMonth() {
        return baseMapper.selectViewCountByMonth();
    }

    @Override
    public List<DateCountVo> getBlogCountByMonth() {
        return baseMapper.selectBlogCountByMonth();
    }

    @Override
    public List<DateCountVo> getAppreciateCountByMonth() {
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
            if (user != null) {
                blog.setUserAvatar(user.getAvatar());
                blog.setUserNickName(user.getNickname());
            } else {
                blog.setUserNickName("用户已注销");
                blog.setUserAvatar(ConstantImg.DEFAULT_AVATAR);
            }
        }, executor);

        CompletableFuture<Void> content = CompletableFuture.runAsync(() -> {
            // 获取正文
            String text = getText(blog.getContent());
            blog.setContent(text);
        }, executor);

        CompletableFuture.runAsync(() -> {
            // 更新阅读量
            int oldv = blog.getViews();
            while (baseMapper.updateViews(id, oldv, oldv + 1) == 0) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {}
                oldv = getById(id).getViews();
            }
        }, executor);

        CompletableFuture.allOf(tag, type, content, author).get();
        return blog;
    }

    @Override
    public List<BlogEntity> search(String query) {
        return baseMapper.selectCondition(query);
    }

    @Override
    public void delBlog(Long id) {
        CompletableFuture.runAsync(() -> {
            // 删除博客内容
            BlogEntity blog = getById(id);
            String img = blog.getFirstPicture();
            if (!StringUtils.isEmpty(img) && !ConstantImg.DEFAULT_IMG.equals(img)) {
                ossUtils.del(blog.getFirstPicture());
            }
            ossUtils.del(blog.getContent());
        }, executor);

        // 删除博客图片
        CompletableFuture.runAsync(() -> {
            List<PicturesEntity> entityList = picturesService.list(new QueryWrapper<PicturesEntity>().eq("belong", id).eq("type", true));
            entityList.forEach(x -> {
                ossUtils.del(x.getImage());
            });
            picturesService.removeByIds(entityList);
        }, executor);

        // 删除博客标签对应关系
        CompletableFuture.runAsync(() -> {
            blogTagsService.remove(new QueryWrapper<BlogTagsEntity>().eq("blogs_id", id));
        }, executor);

        // 删除博客相关评论
        CompletableFuture.runAsync(() -> {
            commentService.remove(new QueryWrapper<CommentEntity>().eq("blog_id", id));
        }, executor);

        removeById(id);
    }

    @Override
    public void updateImg(BlogEntity blog) {
        BlogEntity entity = getById(blog.getId());
        if (!StringUtils.isEmpty(entity.getFirstPicture()) && !ConstantImg.DEFAULT_IMG.equals(entity.getFirstPicture())) {
            ossUtils.del(entity.getFirstPicture());
        }

        updateById(blog);
    }

    @Override
    public BlogEntity getDefaultBlogInfo(Long id) {
        BlogEntity blog = getById(id);
        blog.setContent(new String(ossUtils.load(blog.getContent()), StandardCharsets.UTF_8));
        return blog;
    }

    @Override
    public void updateBlog(BlogEntity blog) {
        BlogEntity old = getById(blog.getId());
        String key = old.getContent();
        if (!StringUtils.isEmpty(key)) {
            ossUtils.del(key);
        }

        String[] split = key.split("/");
        if (split.length > 2) {
            key = ossConfig.getBlog() + split[1] + "/" + UUID.randomUUID();
        }
        blog.setContent(ossUtils.upload(key, blog.getContent().getBytes(StandardCharsets.UTF_8)));

        CompletableFuture.runAsync(() -> {
            updatePictureBelongBlog(blog.getId());
        }, executor);

        updateById(blog);
    }

    @Override
    public boolean addBlog(BlogEntity blog) throws ExecutionException, InterruptedException {
//        TransactionStatus[] status = new TransactionStatus[3];

        CompletableFuture<Long> saveFuture = CompletableFuture.supplyAsync(() -> {
//            status[0] = platformTransactionManager.getTransaction(transactionDefinition);
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setDescription(blog.getContent().substring(0, Math.min(120, blog.getContent().length())));
            if (StringUtils.isEmpty(blog.getFirstPicture())) {
                blog.setFirstPicture(ConstantImg.DEFAULT_IMG);
            }
            String k = ossConfig.getBlog() + LocalDate.now() + "/" + UUID.randomUUID();
            ossUtils.upload(k, blog.getContent().getBytes(StandardCharsets.UTF_8));
            blog.setContent(k);
            blog.setPublished(false);
            blog.setRecommend(true);
            blog.setShareStatement(false);
            save(blog);
            return blog.getId();
        }, executor);

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        CompletableFuture<Void> picturesFuture = saveFuture.thenAcceptAsync(x -> {
//            status[1] = platformTransactionManager.getTransaction(transactionDefinition);
            RequestContextHolder.setRequestAttributes(attributes);
            updatePictureBelongBlog(x);
        }, executor);

        CompletableFuture<Void> blogTagFuture = saveFuture.thenAcceptAsync(a -> {
//            status[2] = platformTransactionManager.getTransaction(transactionDefinition);

            String tagIds = blog.getTagIds();
            String[] split = tagIds.split(",");
            List<BlogTagsEntity> blogTagsEntities = Arrays.stream(split).map(x -> {
                BlogTagsEntity blogTags = new BlogTagsEntity();
                blogTags.setTagsId(Long.parseLong(x));
                blogTags.setBlogsId(a);
                return blogTags;
            }).collect(Collectors.toList());
            blogTagsService.saveBatch(blogTagsEntities);
        }, executor);

//        try {
            CompletableFuture.allOf(picturesFuture, blogTagFuture).get();
//            for (TransactionStatus s : status) {
//                if (s.isCompleted()) {
//                    platformTransactionManager.commit(s);
//                }
//            }
            return true;
//        } catch (Exception e) {
//            for (TransactionStatus s : status) {
//                if (s.isCompleted()) {
//                    platformTransactionManager.rollback(s);
//                }
//            }
//            return false;
//        }
    }

    @Override
    public void updateType(BlogEntity blog) {
        updateById(blog);
    }

    @Override
    public void delTag(DelBlogTagVo vo) {
        blogTagsService.remove(new QueryWrapper<BlogTagsEntity>().eq("blogs_id", vo.getId()).eq("tags_id", vo.getTagId()));
    }

    @Override
    public TagEntity createTag(Long id, String name) {
        TagEntity tag = new TagEntity();
        tag.setName(name);
        tagService.save(tag);

        BlogTagsEntity blogTags = new BlogTagsEntity();
        blogTags.setBlogsId(id);
        blogTags.setTagsId(tag.getId());
        blogTagsService.save(blogTags);
        return tag;
    }

    /**
     * 更新图片所属博客
     * @param blogId
     */
    private void updatePictureBelongBlog(Long blogId) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            Cookie c = null;
            for (Cookie cookie : cookies) {
                if (ConstantImg.IMG_COOKIE_NAME.equals(cookie.getName())) {
                    c = cookie;
                    break;
                }
            }
            if (c != null) {
                long id = Long.parseLong(c.getValue());
                picturesService.updateOldPicturesByLongId(id, blogId, 1);
            }
        }
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
        byte[] bytes = ossUtils.load(content);
        return MarkdownUtils.markdownToHtmlExtensions(new String(bytes, StandardCharsets.UTF_8));
    }

}

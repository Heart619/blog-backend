package com.example.blog2.service.old.impl;

import com.example.blog2.config.OSSConfig;
import com.example.blog2.dao.BlogRepository;
import com.example.blog2.po.Blog;
import com.example.blog2.po.Type;
import com.example.blog2.service.old.BlogService;
import com.example.blog2.service.old.PicturesService;
import com.example.blog2.utils.MarkdownUtils;
import com.example.blog2.utils.MyBeanUtils;
import com.example.blog2.utils.OSSUtils;
import com.example.blog2.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

/**
 * @author mxp
 */
//@Service
public class BlogServiceImpl implements BlogService {

//    @Autowired
    private BlogRepository blogRepository;

//    @Autowired
    private PicturesService picturesService;

//    @Autowired
    private OSSConfig ossConfig;

//    @Autowired
    private OSSUtils OSSUtils;

//    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll((Specification<Blog>) (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
//            根据标题查找
            if (!"".equals(blog.getTitle())&& blog.getTitle() != null) {
                predicates.add(cb.like(root.get("title"),"%"+blog.getTitle()+"%"));
            }
//            根据type对象的id值查找
            if (blog.getTypeId()!=null){
                predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
            }

            cq.where(predicates.toArray(new Predicate[predicates.size()]));
            return null;
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        Page<Blog> blogs = blogRepository.findAll(pageable);
        blogs.stream().forEach( blog -> {
            blog.setContent("");
            blog.setComments(null);
        });
        return blogs;
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        Page<Blog> blogs = blogRepository.findAll((Specification<Blog>) (root, cq, cb) -> cb.equal(root.join("tags").get("id"),tagId),pageable);
        blogs.stream().forEach( blog -> {
            blog.setContent("");
            blog.setComments(null);
        });
        return blogs;
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByQuery(query,pageable);
        blogs.stream().forEach( blog -> {
            blog.setContent("");
            blog.setComments(null);
        });
        return blogs;
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        List<Blog> blogs = blogRepository.findTop(pageable);
        blogs.forEach(blog -> {
            blog.setContent("");
            blog.setComments(null);
        });
        return blogs;
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getFlag() == null || "".equals(blog.getFlag())) {
            blog.setFlag("原创");
        }
        if (blog.getFirstPicture() == null || "".equals(blog.getFirstPicture())) {
            blog.setFirstPicture(blog.getType().getPic_url());
        }
        if (blog.getDescription() == null || "".equals(blog.getDescription())) {
            blog.setDescription(blog.getContent().substring(0, Math.min(120, blog.getContent().length())));
        }
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        String content = blog.getContent();
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        String k = ossConfig.getBlog() + LocalDate.now() + "/" + blog.getTitle();
        String key = OSSUtils.upload(k, bytes);
        blog.setContent(key);
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        String k = b.getContent();
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        if (b.getFirstPicture() == null || "".equals(b.getFirstPicture())) {
            b.setFirstPicture(b.getType().getPic_url());
        }
        if (!StringUtils.isEmpty(blog.getContent())) {
            byte[] bytes = b.getContent().getBytes(StandardCharsets.UTF_8);
            OSSUtils.del(k);
            OSSUtils.upload(k, bytes);
            b.setContent(k);
        }
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteBlog(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (!StringUtils.isEmpty(blog.getFirstPicture()) && !blog.getFirstPicture().equals(blog.getType().getPic_url())) {
            picturesService.delOssImg(blog.getFirstPicture());
        }
        OSSUtils.del(blog.getContent());
        blogRepository.deleteById(id);
    }

    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        blog.setViews(blog.getViews()+1);
        blog = blogRepository.save(blog);
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        byte[] bytes = OSSUtils.load(b.getContent());
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(new String(bytes, StandardCharsets.UTF_8)));
        return b;
    }

    @Override
    public Blog getSimpleBlog(Long id) {
        Blog blog = blogRepository.getOne(id);
        byte[] bytes = OSSUtils.load(blog.getContent());
        blog.setContent(new String(bytes, StandardCharsets.UTF_8));
        return blog;
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String,List<Blog>> map = new HashMap<>();
        for (String year:years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Override
    public Long countViews() {
        return blogRepository.countViews();
    }

    @Override
    public Long countAppreciate() {
        return blogRepository.countAppreciate();
    }

    @Override
    public Long countComment() {
        return blogRepository.countComment();
    }

    @Override
    public List<String> ViewCountByMonth() {
        return blogRepository.ViewCountByMonth();
    }

    @Override
    public List<String> BlogCountByMonth() {
        return blogRepository.BlogCountByMonth();
    }

    @Override
    public List<String> appreciateCountByMonth() {
        return blogRepository.appreciateCountByMonth();
    }

}

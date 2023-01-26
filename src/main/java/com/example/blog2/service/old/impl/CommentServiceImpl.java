//package com.example.blog2.service.old.impl;
//
//import com.example.blog2.dao.CommentRepository;
//import com.example.blog2.po.Blog;
//import com.example.blog2.po.Comment;
//import com.example.blog2.service.CommentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author hikari
// * @version 1.0
// * @date 2021/4/20 15:56
// */
////@Service
//public class CommentServiceImpl implements CommentService {
//
////    @Autowired
//    private CommentRepository commentRepository;
//
//    private List<Comment> tempReplys = new ArrayList<>();
//
//    @Override
//    public List<Comment> listCommentByBlogId(Long blogId) {
//        Sort sort = Sort.by("createTime");
//        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
//        comments.forEach(comment -> {
//            Blog blog = comment.getBlog();
//            blog.setContent("");
//            comment.setBlog(blog);
//        });
//        return comments;
//    }
//
//
//
//
//    @Transactional
//    @Override
//    public Comment saveComment(Comment comment) {
//        comment.setCreateTime(new Date());
//        return commentRepository.save(comment);
//    }
//
//    @Override
//    public List<Comment> listComment() {
//        List<Comment> comments = commentRepository.findAll();
//        comments.forEach(comment -> {
//            Blog blog = comment.getBlog();
//            blog.setContent("");
//            comment.setBlog(blog);
//        });
//        return comments;
//    }
//
//    @Override
//    public List<String> CommentCountByMonth() {
//        return commentRepository.CommentCountByMonth();
//    }
//
//    @Override
//    public Comment getCommentById(Long id) {
//        return commentRepository.getOne(id);
//    }
//
//    @Override
//    public void deleteComment(Long id) {
//        commentRepository.deleteById(id);
//    }
//
//    @Override
//    public List<Comment> getChildrenComment(Long id) {
//        return commentRepository.findChildrenComment(id);
//    }
//}

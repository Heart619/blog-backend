package com.example.blog2.service.old;

import com.example.blog2.po.Comment;

import java.util.List;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/4/20 15:56
 */

public interface CommentService {
    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);

    List<Comment> listComment();

    List<String> CommentCountByMonth();

    Comment getCommentById(Long id);

    void deleteComment(Long id);

    List<Comment> getChildrenComment(Long id);
}

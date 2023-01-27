package com.example.blog2.controller.web;

import com.example.blog2.entity.CommentEntity;
import com.example.blog2.service.CommentService;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mxp
 * @date 2023/1/27 13:50
 */

@RestController
@RequestMapping("/comment")
public class CommentWebController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comments/{blogId}")
    public R comments(@PathVariable Long blogId) {
        List<CommentEntity> commentEntities;
        try {
            commentEntities = commentService.getCommentsByBlogId(blogId);
            return R.ok("获取博客评论成功").put("data", commentEntities);
        } catch (Exception e) {
            return R.error("网络繁忙，请稍后再试");
        }
    }

    @GetMapping("/new/comments")
    public R getNewComments() {
        try {
            List<CommentEntity> commentEntities = commentService.getNewComments();
            return R.ok().put("data", commentEntities);
        } catch (Exception e) {
            return R.error("网络繁忙，请稍后再试");
        }
    }
}

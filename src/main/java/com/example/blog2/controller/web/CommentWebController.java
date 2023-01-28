package com.example.blog2.controller.web;

import com.example.blog2.entity.CommentEntity;
import com.example.blog2.service.CommentService;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import com.example.blog2.vo.BlogCommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mxp
 * @date 2023/1/27 13:50
 */

@RestController
@RequestMapping("/comment")
public class CommentWebController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = commentService.queryBlogCommentPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/comments/{blogId}")
    public R comments(@PathVariable Long blogId) {
        List<CommentEntity> commentEntities = commentService.getCommentsByBlogId(blogId);
        return R.ok("获取博客评论成功").put("data", commentEntities);
    }

    @GetMapping("/new/comments")
    public R getNewComments() {
        List<CommentEntity> commentEntities = commentService.getNewComments();
        return R.ok().put("data", commentEntities);
    }

    @RequestMapping("/save")
    public R save(@RequestBody CommentEntity comment){
        comment.setCreateTime(new Date());
        BlogCommentVo vo = commentService.addComment(comment);
        return R.ok().put("data", vo);
    }

    @GetMapping("/getCmt/{blog}/{cid}")
    public R getCmtByPmt(@PathVariable("blog") Long blog, @PathVariable("cid") Long cid) {
        List<BlogCommentVo> vos = commentService.getCmtByPmt(blog, cid);
        return R.ok().put("data", vos);
    }
}

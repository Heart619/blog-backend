//package com.example.blog2.controller.web;
//
//import com.example.blog2.po.Comment;
//import com.example.blog2.po.Result;
//import com.example.blog2.po.StatusCode;
//import com.example.blog2.po.User;
//import com.example.blog2.service.old.BlogService;
//import com.example.blog2.service.old.CommentService;
//import com.example.blog2.service.old.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.*;
//
//
//import java.util.List;
//import java.util.Map;
//
//
///**
// * @author hikari
// * @version 1.0
// * @date 2021/4/20 9:58
// */
////@RestController
//public class CommentController {
//
////    @Autowired
//    private CommentService commentService;
//
////    @Autowired
//    private BlogService blogService;
//
////    @Autowired
//    private UserService userService;
//
//    private final String avatar = "/images/avatar.png";
//
//    //获取评论集合
//    @GetMapping("/comments/{blogId}")
//    public Result comments(@PathVariable Long blogId) {
//        return new Result(true, StatusCode.OK, "获取博客评论成功", commentService.listCommentByBlogId(blogId));
//    }
//
//    @PostMapping("/comments")
//    public Result post(@RequestBody Map<String,Object> para) {
//        String content= (String) para.get("content");
//        Long blogId= Long.parseLong( para.get("blogId").toString());
//        Long userId= Long.parseLong( para.get("userId").toString());
//        long parentId= Long.parseLong( para.get("parentId").toString());
//        User user = userService.findUserById(userId);
//        Comment comment = new Comment();
//        comment.setContent(content);
//        comment.setBlog(blogService.getBlog(blogId));
//        comment.setUserId(userId);
//        comment.setNickname(user.getNickname());
//        comment.setEmail(user.getEmail());
//        comment.setAvatar(user.getAvatar());
//        comment.setAdminComment(user.getType().equals("1"));
//        if (parentId != -1){
//            comment.setParentComment(commentService.getCommentById(parentId));
//        }
//        Comment newComment = commentService.saveComment(comment);
//        return new Result(true,StatusCode.OK,"评论发表成功！",newComment);
//    }
//
//    //删除评论
//    @GetMapping("/comments/{id}/delete")
//    public Result delete(@PathVariable Long id) {
//        List<Comment> children = commentService.getChildrenComment(id);
//        if (!CollectionUtils.isEmpty(children)) {
//            children.forEach(x -> {
//                commentService.deleteComment(x.getId());
//            });
//        }
//        commentService.deleteComment(id);
//        return new Result(true, StatusCode.OK, "删除评论成功", null);
//    }
//
//}

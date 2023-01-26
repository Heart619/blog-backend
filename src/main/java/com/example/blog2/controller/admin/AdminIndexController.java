//package com.example.blog2.controller.admin;
//
//import com.example.blog2.po.Result;
//import com.example.blog2.po.StatusCode;
//import com.example.blog2.service.CommentService;
//import com.example.blog2.service.TagService;
//import com.example.blog2.service.TypeService;
//import com.example.blog2.service.old.UserService;
//import com.example.blog2.service.old.BlogService;
//import org.springframework.web.bind.annotation.*;
//
///**
// * @author mxp
// */
//
////@RestController
////@RequestMapping("/admin")
//public class AdminIndexController {
//
////    @Autowired
//    private BlogService blogService;
//
////    @Autowired
//    private TypeService typeService;
//
////    @Autowired
//    private TagService tagService;
//
////    @Autowired
//    private UserService userService;
//
////    @Autowired
//    private CommentService commentService;
//
//    //获取博客数量
//    @GetMapping("/getBlogCount")
//    public Result getBlogList() {
//        return new Result(true, StatusCode.OK, "获取博客总数成功", blogService.countBlog());
//    }
//    //获取总阅读量
//    @GetMapping("/getViewCount")
//    public Result getViewCount() {
//        return new Result(true, StatusCode.OK, "获取阅读总数成功", blogService.countViews());
//    }
//
//    //获取总点赞数
//    @GetMapping("/getAppreciateCount")
//    public Result getAppreciateCounts() {
//        return new Result(true, StatusCode.OK, "获取赞赏总数成功", blogService.countAppreciate());
//    }
//
//    //获取总评论数
//    @GetMapping("/getCommentCount")
//    public Result getCommentCount() {
//        return new Result(true, StatusCode.OK, "获取评论总数成功", blogService.countComment());
//    }
//
//    //根据月份统计阅读量
//    @GetMapping("/getViewCountByMonth")
//    public Result getBlogViewsByMonth() {
//        return new Result(true, StatusCode.OK, "获取按月份统计阅读总数成功", blogService.ViewCountByMonth());
//    }
//
//    //根据月份统计博客发表数
//    @GetMapping("/getBlogCountByMonth")
//    public Result getBlogCountByMonth() {
//        return new Result(true, StatusCode.OK, "获取按月份统计发表总数成功", blogService.BlogCountByMonth());
//    }
//
//    //根据月份统计评论数
//    @GetMapping("/getCommentCountByMonth")
//    public Result getCommentCountByMonth() {
//        return new Result(true, StatusCode.OK, "获取按月份统计评论总数成功", commentService.list());
//    }
//
//    //根据月份统计评论数
//    @GetMapping("/getAppreciateCountByMonth")
//    public Result getAppreciateCountByMonth() {
//        return new Result(true, StatusCode.OK, "获取按月份统计评论总数成功", blogService.appreciateCountByMonth());
//    }
//
//    @GetMapping("/getFullTagList")
//    public Result getFullTagList() {
//        return new Result(true, StatusCode.OK, "获取所有博客标签成功", tagService.list());
//    }
//
//    @GetMapping("/getFullTypeList")
//    public Result getFullTypeList() {
//        return new Result(true, StatusCode.OK, "获取博客全部分类成功",typeService.getAllType());
//    }
//
//    @GetMapping("/getCommentList")
//    public Result getCommentList() {
//        return new Result(true, StatusCode.OK, "获取评论列表成功",commentService.list());
//    }
//
//    @GetMapping("/getUserAreaList")
//    public Result getuserAreaList(){
//        return new Result(true,StatusCode.OK,"获取用户地址列表成功",userService.listUser());
//    }
//}

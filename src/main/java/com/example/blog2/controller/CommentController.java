package com.example.blog2.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.blog2.entity.CommentEntity;
import com.example.blog2.service.CommentService;



/**
 * @author mxp
 * @date 2023-01-25 09:47:20
 */
@RestController
@RequestMapping("/admin/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = commentService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CommentEntity comment = commentService.getById(id);

        return R.ok().put("comment", comment);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CommentEntity comment){
		commentService.updateById(comment);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		commentService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @GetMapping("/{id}/delete")
    public R delete(@PathVariable Long id) {
        try {
            commentService.delComment(id);
            return R.ok("删除成功");
        } catch (Exception e) {
            return R.error("网络繁忙，请稍后再试");
        }
    }

    @GetMapping("/getCommentCountByMonth")
    public R getCommentCountByMonth() {
        List<String> res = commentService.getCommentCountByMonth();
        return R.ok().put("data", res);
    }

    @GetMapping("/getCommentCount")
    public R getCommentCount() {
        Long count = commentService.getCommentCount();
        return R.ok().put("data", count);
    }
}

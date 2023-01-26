package com.example.blog2.controller.admin;

import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.po.Tag;
import com.example.blog2.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/4/1 11:41
 */
//@RestController
//@RequestMapping("/admin")
//public class TagController {
//
//    @Autowired
//    private TagService tagService;
//
////    添加或修改标签
//    @PostMapping("/tags")
//    public Result post(@RequestBody Map<String, Tag> para) {
//        Tag tag = para.get("tag");
//        if (tag.getId() != null){
//            Tag tag1 = tagService.getTagByName(tag.getName());
//            if (tag1 != null) {
//                return new Result(false, StatusCode.ERROR, "不能添加重复的标签",null);
//            }
//        } else {
//            List<Tag> tagList = tagService.listByNameExceptSelf(tag.getId(),tag.getName());
//            if (tagList.size() >0 ) {
//                return new Result(false, StatusCode.ERROR, "分类名称已存在", null);
//            }
//        }
//
//        Tag t = tagService.saveTag(tag);
//        if (t == null) {
//            return new Result(false, StatusCode.ERROR, "修改失败", null);
//        }
//        return new Result(true, StatusCode.OK, "修改成功", t);
//    }
//
//
//    @GetMapping("/tags/{id}/delete")
//    public Result delete(@PathVariable Long id) {
//        tagService.deleteTag(id);
//        return new Result(true, StatusCode.OK, "删除成功", null);
//    }
//
//}

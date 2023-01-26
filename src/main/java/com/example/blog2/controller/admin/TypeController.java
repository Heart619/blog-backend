//package com.example.blog2.controller.admin;
//
//import com.example.blog2.po.Result;
//import com.example.blog2.po.StatusCode;
//import com.example.blog2.po.Type;
//import com.example.blog2.service.old.TypeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * @author hikari
// * @version 1.0
// * @date 2021/4/4 11:21
// */
//
////@RestController
////@RequestMapping("/admin")
//public class TypeController {
//
////    @Autowired
//    private TypeService typeService;
//
////    新增或删除type
//    @PostMapping("/types")
//    public Result post(@RequestBody Map<String, Type> para) {
//        Type type = para.get("type");
//        if (type.getId() == null){
//            Type type1 = typeService.getTypeByName(para.get("type").getName());
//            if (type1 != null) {
//                return new Result(false, StatusCode.ERROR, "不能添加重复的分类", null);
//            } else {
//                Type saveType = typeService.saveType(type);
//                return new Result(true, StatusCode.OK, "添加成功", null);
//            }
//        } else {
//            List<Type> typeList = typeService.listByNameExceptSelf(type.getId(),type.getName());
//            if (typeList.size() > 0) {
//                return new Result(false, StatusCode.ERROR, "分类名称已存在", null);
//            }
//        }
//        Type t = typeService.updateType(type.getId(), type);
//        if (t == null) {
//            return new Result(false, StatusCode.ERROR, "修改失败", null);
//        }
//        return new Result(true, StatusCode.OK, "修改成功", null);
//    }
//
//
//    @GetMapping("/types/{id}/delete")
//    public Result delete(@PathVariable Long id) {
//        typeService.deleteType(id);
//        return new Result(true, StatusCode.OK, "删除成功", null);
//    }
//
//}

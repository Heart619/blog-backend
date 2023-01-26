//package com.example.blog2.controller.web;
//
//import com.example.blog2.po.*;
//import com.example.blog2.service.old.MessageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
///**
// * @author hikari
// * @version 1.0
// * @date 2021/7/13 11:25
// */
////@RestController
//public class MessageShowController {
////    @Autowired
//    private MessageService messageService;
//
//    @GetMapping("/messages")
//    public Result messages() {
//        return new Result(true, StatusCode.OK, "获取留言列表成功",messageService.listMessage());
//    }
//
//    @PostMapping("/messages/del/{id}")
//    public Result delMsg(@PathVariable("id") Long id) {
//        try {
//            messageService.deleteMessage(id);
//            return new Result(true, StatusCode.OK, "留言删除成功成功", messageService.listMessage());
//        } catch (Exception e) {
//            return new Result(true, StatusCode.ERROR, "当前网络繁忙，就稍后再试", messageService.listMessage());
//        }
//    }
//
//    @PostMapping("/messages")
//    public Result post(@RequestBody Map<String, Message> para) {
//        Message message = para.get("message");
//        Message m;
//        if (message.getId() == null){
//            m = messageService.saveMessage(message);
//        } else {
//            m = messageService.updateMessage(message.getId(),message);
//        }
//        if (m == null) {
//            return new Result(false,StatusCode.ERROR,"操作失败");
//        }
//        return new Result(true,StatusCode.OK,"操作成功");
//    }
//
//}

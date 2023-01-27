package com.example.blog2.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.blog2.entity.MessageEntity;
import com.example.blog2.service.MessageService;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author mxp
 * @date 2023/1/27 13:55
 */

@RestController
@RequestMapping("/message")
public class MessageWebController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/addMessage")
    public R save(@RequestBody MessageEntity message){
        try {
            message.setCreateTime(new Date());
            messageService.save(message);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }

    @GetMapping("/messages")
    public R messages() {
        return R.ok("获取留言列表成功").put("data", messageService.list(new QueryWrapper<MessageEntity>().orderByDesc("create_time")));
    }
}

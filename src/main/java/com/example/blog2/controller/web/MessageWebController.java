package com.example.blog2.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.blog2.entity.MessageEntity;
import com.example.blog2.service.MessageService;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * @author mxp
 * @date 2023/1/27 13:55
 */

@RestController
@RequestMapping("/message")
public class MessageWebController {

    @Autowired
    private MessageService messageService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = messageService.queryPage(params);

        return R.ok("获取留言列表成功").put("page", page);
    }

    @PostMapping("/addMessage")
    public R save(@RequestBody MessageEntity message){
        message.setCreateTime(new Date());
        messageService.saveMessage(message);
        return R.ok().put("data", message);
    }
}

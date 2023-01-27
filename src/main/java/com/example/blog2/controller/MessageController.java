package com.example.blog2.controller;

import java.util.Arrays;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.blog2.entity.MessageEntity;
import com.example.blog2.service.MessageService;



/**
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@RestController
@RequestMapping("/admin/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = messageService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MessageEntity message = messageService.getById(id);

        return R.ok().put("message", message);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MessageEntity message){
		messageService.updateById(message);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		messageService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @PostMapping("/messages/del/{id}")
    public R delMsg(@PathVariable("id") Long id) {
        try {
            messageService.removeById(id);
            return R.ok("留言删除成功成功").put("data", messageService.list(new QueryWrapper<MessageEntity>().orderByDesc("create_time")));
        } catch (Exception e) {
            return R.error("当前网络繁忙，就稍后再试");
        }
    }
}

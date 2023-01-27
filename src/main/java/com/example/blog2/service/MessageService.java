package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.entity.MessageEntity;
import com.example.blog2.utils.PageUtils;

import java.util.Map;

/**
 *
 *
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
public interface MessageService extends IService<MessageEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 删除用户时，清楚用户留言
     * @param id
     */
    void userDelUpdateMessage(Long id);
}


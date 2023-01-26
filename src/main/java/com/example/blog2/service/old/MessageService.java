package com.example.blog2.service.old;

import com.example.blog2.po.Message;

import java.util.List;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/13 11:20
 */
public interface MessageService {
    List<Message> listMessage();

    void deleteMessage(Long id);

    Message saveMessage(Message message);

    Message updateMessage(Long id,Message message);
}

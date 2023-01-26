package com.example.blog2.service.old.impl;

import com.example.blog2.dao.MessageRepository;
import com.example.blog2.po.Message;
import com.example.blog2.service.old.MessageService;
import com.example.blog2.utils.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author  hikari
 * @date  2021/7/13 11:21
 * @version 1.0
 */
//@Service
public class MessageServiceImpl implements MessageService {
//    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> listMessage() {
        return messageRepository.findAll();
    }

    @Override
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public Message saveMessage(Message message) {
        message.setCreateTime(new Date());
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessage(Long id, Message message) {
        Message m = messageRepository.getOne(id);
        BeanUtils.copyProperties(message,m, MyBeanUtils.getNullPropertyNames(message));
        return messageRepository.save(m);
    }
}

package com.example.blog2.service.impl;

import com.example.blog2.constant.ConstantImg;
import com.example.blog2.interceptor.IPInterceptor;
import com.example.blog2.utils.Constant;
import com.example.blog2.utils.DefaultImgUtils;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.MessageDao;
import com.example.blog2.entity.MessageEntity;
import com.example.blog2.service.MessageService;


/**
 * @author mxp
 */

@Slf4j
@Service
public class MessageServiceImpl extends ServiceImpl<MessageDao, MessageEntity> implements MessageService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MessageEntity> page = this.page(
                new Query<MessageEntity>().getPage(params),
                new QueryWrapper<MessageEntity>().orderByDesc("create_time")
        );

//        log.info("IP：{}， 获取留言信息", IPInterceptor.IP_INFO.get());
        return new PageUtils(page);
    }

    @Override
    public void userDelUpdateMessage(Long id) {
        this.baseMapper.updateUserMessage(id, DefaultImgUtils.getDefaultAvatarImg());
    }

    @Override
    public void updateMessageForUserUpdate(Long id, String nickname) {
        baseMapper.updateMessageForUserUpdate(id, nickname);
    }

    @Override
    public void saveMessage(MessageEntity message) {
        save(message);
        log.info("IP：{}， 用户[{}] - {}， 新增留言：[{}]", IPInterceptor.IP_INFO.get(), message.getUserId(), message.getNickname(), message.getContent());
    }

}

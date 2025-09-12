package com.wly.config.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wly.config.dao.entity.Message;
import com.wly.config.dao.mapper.MessageMapper;

import java.util.List;

public class MessageService extends ServiceImpl<MessageMapper, Message> {

    public List<Message> queryFromOffset(long offset, int limit) {
        return list(Wrappers.< Message>lambdaQuery().ge(Message::getId, offset).last("limit " + limit));
    }
}

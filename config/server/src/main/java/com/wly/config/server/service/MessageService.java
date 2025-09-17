package com.wly.config.server.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wly.config.server.dao.entity.Message;
import com.wly.config.server.dao.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService extends ServiceImpl<MessageMapper, Message> {

    public List<Message> queryFromOffset(long offset, int limit) {
        return list(Wrappers.< Message>lambdaQuery().ge(Message::getId, offset).last("limit " + limit));
    }
}

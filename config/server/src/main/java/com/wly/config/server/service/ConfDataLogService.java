package com.wly.config.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wly.config.server.dao.entity.ConfDataLog;
import com.wly.config.server.dao.mapper.ConfDataLogMapper;
import org.springframework.stereotype.Service;

@Service
public class ConfDataLogService extends ServiceImpl<ConfDataLogMapper, ConfDataLog> {
}

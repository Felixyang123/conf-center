package com.wly.config.server.dao.rep;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wly.config.server.dao.entity.ConfAccessToken;
import com.wly.config.server.dao.mapper.ConfAccessTokenMapper;
import org.springframework.stereotype.Service;

@Service
public class ConfAccessTokenRepository extends ServiceImpl<ConfAccessTokenMapper, ConfAccessToken> {
}

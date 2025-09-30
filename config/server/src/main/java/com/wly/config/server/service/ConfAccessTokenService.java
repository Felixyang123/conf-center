package com.wly.config.server.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wly.config.server.dao.entity.ConfAccessToken;
import com.wly.config.server.dao.rep.ConfAccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfAccessTokenService {
    private final ConfAccessTokenRepository confAccessTokenRepository;

    public boolean checkAccessToken(String accessToken, String appname, String env) {
        ConfAccessToken confAccessToken = confAccessTokenRepository.getOne(Wrappers.<ConfAccessToken>lambdaQuery()
                .eq(ConfAccessToken::getAppname, appname).eq(ConfAccessToken::getEnv, env));

        if (confAccessToken == null) {
            throw new RuntimeException("应用未注册");
        }

        if (!Objects.equals(confAccessToken.getStatus(), 0)) {
            throw new RuntimeException("应用未启用");
        }

        if (!Objects.equals(confAccessToken.getAccessToken(), accessToken)) {
            throw new RuntimeException("accessToken错误");
        }

        return true;
    }

    public String registerAccessToken(String appname, String env) {
        String accessToken = generateAccessToken(appname, env);
        int upsert = confAccessTokenRepository.getBaseMapper().upsert(ConfAccessToken.builder().accessToken(accessToken).appname(appname).env(env).build());
        if (upsert <= 0) {
            throw new RuntimeException("注册失败");
        }
        return accessToken;
    }

    public String generateAccessToken(String appname, String env) {
        //TODO 扩展TOKEN生成方式
        return UUID.randomUUID().toString();
    }
}

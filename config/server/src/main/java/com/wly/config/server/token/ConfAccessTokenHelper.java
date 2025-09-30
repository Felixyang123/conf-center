package com.wly.config.server.token;

import com.wly.config.server.service.ConfAccessTokenService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ConfAccessTokenHelper implements ApplicationContextAware {
    private static ConfAccessTokenService tokenService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ConfAccessTokenHelper.tokenService = applicationContext.getBean(ConfAccessTokenService.class);
    }

    public static boolean checkAccessToken(String accessToken, String appname, String env) {
        return tokenService.checkAccessToken(accessToken, appname, env);
    }
}

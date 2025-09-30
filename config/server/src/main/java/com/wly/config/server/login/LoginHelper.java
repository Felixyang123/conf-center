package com.wly.config.server.login;

import com.wly.sso.core.handler.LoginContext;
import com.wly.sso.core.handler.LoginHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class LoginHelper implements ApplicationContextAware {

    private static LoginHandler loginHandler;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LoginHelper.loginHandler = applicationContext.getBean(LoginHandler.class);
    }

    public static void login(LoginContext ctx) {
        loginHandler.login(ctx);
    }

    public static void logout(LoginContext ctx) {
        loginHandler.logout(ctx);
    }
}

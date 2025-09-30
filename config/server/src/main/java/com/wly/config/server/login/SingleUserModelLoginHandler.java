package com.wly.config.server.login;

import com.wly.sso.core.bean.LoginInfo;
import com.wly.sso.core.config.LoginPropsConfig;
import com.wly.sso.core.handler.CookieLoginHandler;
import com.wly.sso.core.handler.LoginContext;
import com.wly.sso.core.helper.AuthHelper;
import com.wly.sso.core.helper.TokenHelper;
import com.wly.sso.core.storage.LoginStorage;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Component
public class SingleUserModelLoginHandler extends CookieLoginHandler {

    public SingleUserModelLoginHandler(LoginPropsConfig loginPropsConfig,
                                       LoginStorage loginStorage,
                                       AuthHelper authHelper,
                                       TokenHelper tokenHelper) {
        super(loginPropsConfig, loginStorage, authHelper, tokenHelper);
    }

    @Override
    public boolean checkAccountAndUserValidity(LoginContext ctx) {
        LoginInfo loginInfo = ctx.getLoginInfo();
        return !StringUtils.hasText(ctx.getUserId()) || Objects.equals(ctx.getUserId(), loginInfo.getAccountId());
    }
}

package com.wly.config.server.service;

import com.wly.config.server.dao.entity.User;
import com.wly.config.server.login.LoginHelper;
import com.wly.sso.core.handler.LoginContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserService userService;

    public String login(String username, String password) {
        User user = userService.checkLogin(username, password);

        String userId = String.valueOf(user.getId());
        LoginContext context = LoginContext.builder().username(username).accountId(userId).userId(userId).build();
        LoginHelper.login(context);

        return context.getToken();
    }

    public void logout() {
        LoginHelper.logout(new LoginContext());
    }

    public void register(String username, String pwd, String dupPwd) {
        if (!Objects.equals(pwd, dupPwd)) {
            throw new RuntimeException("密码不一致");
        }

        userService.register(username, pwd);
    }
}

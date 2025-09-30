package com.wly.config.server.controller;

import com.wly.config.server.common.Result;
import com.wly.config.server.common.Results;
import com.wly.config.server.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录相关
 */
@RestController
@RequestMapping("/admin/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping
    public Result<Void> login(String username, String password) {
        loginService.login(username, password);
        return Results.ok();
    }

    /**
     * 登出
     * @return
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        loginService.logout();
        return Results.ok();
    }

    /**
     * 注册
     * @param username
     * @param pwd
     * @param dupPwd
     * @return
     */
    @PostMapping("/register")
    public Result<Void> register(String username, String pwd, String dupPwd) {
        loginService.register(username, pwd, dupPwd);
        return Results.ok();
    }
}

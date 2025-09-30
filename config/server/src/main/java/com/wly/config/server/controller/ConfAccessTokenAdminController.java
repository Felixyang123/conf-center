package com.wly.config.server.controller;

import com.wly.config.server.common.Result;
import com.wly.config.server.common.Results;
import com.wly.config.server.service.ConfAccessTokenService;
import com.wly.sso.core.annotation.SsoCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/access-token")
@RequiredArgsConstructor
public class ConfAccessTokenAdminController {
    private final ConfAccessTokenService confAccessTokenService;

    /**
     * 注册访问令牌
     * @param appname
     * @param env
     * @return
     */
    @SsoCheck
    @PostMapping("/register")
    public Result<String> registerAccessToken(@RequestParam("appname") String appname, @RequestParam("env") String env) {
        String accessToken = confAccessTokenService.registerAccessToken(appname, env);
        return Results.ok(accessToken);
    }
}

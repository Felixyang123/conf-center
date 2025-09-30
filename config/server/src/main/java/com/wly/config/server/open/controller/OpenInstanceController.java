package com.wly.config.server.open.controller;

import com.wly.config.server.helper.bean.PushClientEnvAppDTO;
import com.wly.config.server.open.pojo.OpenApiResp;
import com.wly.config.server.open.pojo.req.OpenInstanceDiscoveryReq;
import com.wly.config.server.open.pojo.req.OpenInstanceRegisterReq;
import com.wly.config.server.open.pojo.resp.OpenInstanceDiscoveryResp;
import com.wly.config.server.service.ConfInstanceService;
import com.wly.config.server.token.AccessTokenCheck;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * 服务注册、发现 open api
 */
@RestController
@RequestMapping("/open/instance")
public class OpenInstanceController {
    @Resource
    private ConfInstanceService instanceService;

    /**
     * 注册实例
     *
     * @param req
     * @return
     */
    @AccessTokenCheck(tokenExpr = "#req.accessToken", envExpr = "#req.env", appExpr = "#req.srcApp")
    @PostMapping("/register")
    public OpenApiResp<Void> register(@RequestBody OpenInstanceRegisterReq req) {
        instanceService.register(req);
        return OpenApiResp.ok();
    }

    /**
     * 取消实例注册
     *
     * @param req
     * @return
     */
    @AccessTokenCheck(tokenExpr = "#req.accessToken", envExpr = "#req.env", appExpr = "#req.srcApp")
    @PostMapping("/unregister")
    public OpenApiResp<Void> unregister(@RequestBody OpenInstanceRegisterReq req) {
        instanceService.unregister(req);
        return OpenApiResp.ok();
    }

    /**
     * 服务发现
     *
     * @param req
     * @return
     */
    @AccessTokenCheck(tokenExpr = "#req.accessToken", envExpr = "#req.env", appExpr = "#req.srcApp")
    @PostMapping("/discovery")
    public OpenApiResp<OpenInstanceDiscoveryResp> discovery(@RequestBody OpenInstanceDiscoveryReq req) {
        return OpenApiResp.ok(instanceService.discovery(req));
    }

    /**
     * 服务订阅
     * @param req
     * @return
     */
    @AccessTokenCheck(tokenExpr = "#req.accessToken", envExpr = "#req.env", appExpr = "#req.srcApp")
    @PostMapping("/watch")
    public DeferredResult<OpenApiResp<PushClientEnvAppDTO>> watch(@RequestBody OpenInstanceDiscoveryReq req) {
        return instanceService.watch(req);
    }
}

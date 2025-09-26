package com.wly.config.samples.controller;

import com.wly.config.core.bean.pojo.req.OpenInstanceDiscoveryReq;
import com.wly.config.core.bean.pojo.req.OpenInstanceRegisterReq;
import com.wly.config.core.bean.pojo.resp.OpenInstanceDiscoveryResp;
import com.wly.config.core.client.RegistryClient;
import com.wly.config.core.config.RegistryClientProps;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample/registry")
public class RegistrySamplesController {
    @Resource
    private RegistryClient registryClient;
    @Resource
    private RegistryClientProps props;

    @PostMapping("/registry")
    public void registry(@RequestBody OpenInstanceRegisterReq req) {
        req.setAccessToken(props.getAccessToken());
        req.setEnv(props.getEnv());
        req.setHeartbeatInterval(props.getHeartbeatInterval());
        registryClient.register(props.getServerAddress(), req);
    }

    @PostMapping("/unregistry")
    public void unregistry(@RequestBody OpenInstanceRegisterReq req) {
        req.setEnv(props.getEnv());
        req.setAccessToken(props.getAccessToken());
        registryClient.unregister(props.getServerAddress(), req);
    }

    @PostMapping("/discovery")
    public OpenInstanceDiscoveryResp discovery(@RequestBody OpenInstanceDiscoveryReq req) {
        req.setAccessToken(props.getAccessToken());
        req.setEnv(props.getEnv());
        return registryClient.discovery(props.getServerAddress(), req);
    }

}

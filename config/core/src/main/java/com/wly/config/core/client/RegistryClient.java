package com.wly.config.core.client;

import com.wly.config.core.bean.PushClientEnvAppDTO;
import com.wly.config.core.bean.pojo.OpenApiResp;
import com.wly.config.core.bean.pojo.req.OpenInstanceDiscoveryReq;
import com.wly.config.core.bean.pojo.req.OpenInstanceRegisterReq;
import com.wly.config.core.bean.pojo.resp.OpenInstanceDiscoveryResp;
import org.springframework.core.ParameterizedTypeReference;

public record RegistryClient(HttpClient httpClient) {

    public OpenInstanceDiscoveryResp discovery(String serverAddress, OpenInstanceDiscoveryReq req) {
        OpenApiResp<OpenInstanceDiscoveryResp> discoveryResult = httpClient.post(serverAddress + "/open/instance/discovery", req,
                new ParameterizedTypeReference<OpenApiResp<OpenInstanceDiscoveryResp>>() {
                });
        return discoveryResult.getData();
    }

    public PushClientEnvAppDTO watch(String serverAddress, OpenInstanceDiscoveryReq req) {
        OpenApiResp<PushClientEnvAppDTO> watchResult = httpClient.post(serverAddress + "/open/instance/watch", req,
                new ParameterizedTypeReference<OpenApiResp<PushClientEnvAppDTO>>() {
                });
        return watchResult.getData();
    }

    public void register(String serverAddress, OpenInstanceRegisterReq req) {
        httpClient.post(serverAddress + "/open/instance/register", req, new ParameterizedTypeReference<OpenApiResp<Void>>() {
        });
    }

    public void unregister(String serverAddress, OpenInstanceRegisterReq req) {
        httpClient.post(serverAddress + "/open/instance/unregister", req, new ParameterizedTypeReference<OpenApiResp<Void>>() {
        });
    }
}

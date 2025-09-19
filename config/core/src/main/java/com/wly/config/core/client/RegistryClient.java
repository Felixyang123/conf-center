package com.wly.config.core.client;

import com.wly.config.core.bean.PushClientEnvAppDTO;
import com.wly.config.core.bean.pojo.OpenApiResp;
import com.wly.config.core.bean.pojo.req.OpenInstanceDiscoveryReq;
import com.wly.config.core.bean.pojo.req.OpenInstanceRegisterReq;
import com.wly.config.core.bean.pojo.resp.OpenInstanceDiscoveryResp;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

public record RegistryClient(HttpClient httpClient) {

    public OpenInstanceDiscoveryResp discovery(String serverAddress, String accessToken, String env, List<String> appnames) {
        OpenInstanceDiscoveryReq req = new OpenInstanceDiscoveryReq();
        req.setEnv(env);
        req.setAccessToken(accessToken);
        req.setAppnames(appnames);
        OpenApiResp<OpenInstanceDiscoveryResp> discoveryResult = httpClient.post(serverAddress + "/open/instance/discovery", req,
                new ParameterizedTypeReference<OpenApiResp<OpenInstanceDiscoveryResp>>() {
                });
        return discoveryResult.getData();
    }

    public PushClientEnvAppDTO watch(String serverAddress, String accessToken, String env, List<String> appnames) {
        OpenInstanceDiscoveryReq req = new OpenInstanceDiscoveryReq();
        req.setEnv(env);
        req.setAccessToken(accessToken);
        req.setAppnames(appnames);
        OpenApiResp<PushClientEnvAppDTO> watchResult = httpClient.post(serverAddress + "/open/instance/watch", req,
                new ParameterizedTypeReference<OpenApiResp<PushClientEnvAppDTO>>() {
                });
        return watchResult.getData();
    }

    public void register(String serverAddress, String accessToken, String env, String appname, String ip, String port, String ext) {
        OpenInstanceRegisterReq req = new OpenInstanceRegisterReq();
        req.setEnv(env);
        req.setAccessToken(accessToken);
        req.setAppname(appname);
        req.setIp(ip);
        req.setPort(port);
        req.setExt(ext);
        httpClient.post(serverAddress + "/open/instance/register", req, new ParameterizedTypeReference<OpenApiResp<Void>>() {
        });
    }

    public void unregister(String serverAddress, String accessToken, String env, String appname, String ip, String port, String ext) {
        OpenInstanceRegisterReq req = new OpenInstanceRegisterReq();
        req.setEnv(env);
        req.setAccessToken(accessToken);
        req.setAppname(appname);
        req.setIp(ip);
        req.setPort(port);
        req.setExt(ext);
        httpClient.post(serverAddress + "/open/instance/unregister", req, new ParameterizedTypeReference<OpenApiResp<Void>>() {
        });
    }
}

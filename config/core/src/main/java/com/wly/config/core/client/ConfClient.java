package com.wly.config.core.client;


import com.wly.config.core.bean.PushClientEnvAppDTO;
import com.wly.config.core.bean.pojo.OpenApiResp;
import com.wly.config.core.bean.pojo.req.OpenDataConfQueryReq;
import com.wly.config.core.bean.pojo.resp.OpenDataConfQueryResp;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;

public record ConfClient(HttpClient httpClient) {
    public OpenDataConfQueryResp query(String serverAddress, String accessToken, String env, Map<String, List<String>> appKeys) {
        OpenDataConfQueryReq req = new OpenDataConfQueryReq();
        req.setEnv(env);
        req.setAccessToken(accessToken);
        req.setQueryDetail(true);
        req.setAppKeys(appKeys);
        OpenApiResp<OpenDataConfQueryResp> openApiResp = httpClient.post(serverAddress + "/open/data/query", req, new ParameterizedTypeReference<OpenApiResp<OpenDataConfQueryResp>>() {
        });
        return openApiResp.getData();
    }

    public PushClientEnvAppDTO watch(String serverAddress, String accessToken, String env, Map<String, List<String>> appKeys) {
        OpenDataConfQueryReq req = new OpenDataConfQueryReq();
        req.setEnv(env);
        req.setAccessToken(accessToken);
        req.setQueryDetail(false);
        req.setAppKeys(appKeys);
        OpenApiResp<PushClientEnvAppDTO> openApiResp = httpClient.post(serverAddress + "/open/data/watch", req, new ParameterizedTypeReference<OpenApiResp<PushClientEnvAppDTO>>() {
        });
        return openApiResp.getData();
    }
}

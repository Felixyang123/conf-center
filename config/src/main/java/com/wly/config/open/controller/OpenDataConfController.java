package com.wly.config.open.controller;

import com.wly.config.helper.DataConfCacheHelper;
import com.wly.config.helper.DeferredResultHandler;
import com.wly.config.helper.bean.ConfDataDTO;
import com.wly.config.helper.bean.PushClientEnvAppDTO;
import com.wly.config.open.pojo.OpenApiResp;
import com.wly.config.open.pojo.req.OpenDataConfQueryReq;
import com.wly.config.open.pojo.resp.OpenDataConfQueryResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * open api 配置查询
 */
@RestController
@RequestMapping("/open/data")
@RequiredArgsConstructor
public class OpenDataConfController {
    private final DataConfCacheHelper dataConfCacheHelper;
    private final DeferredResultHandler deferredResultHandler;

    /**
     * 查询配置
     *
     * @param req
     * @return
     */
    @PostMapping("/query")
    public OpenApiResp<OpenDataConfQueryResp> query(@RequestBody OpenDataConfQueryReq req) {

        Map<String, Map<String, String>> confDataMd5Map = new HashMap<>();
        Map<String, Map<String, ConfDataDTO>> confDataMap = new HashMap<>();
        String env = req.getEnv();

        req.getAppKeys().forEach((appname, keys) -> {
            Map<String, ConfDataDTO> innerConfDataMap = new HashMap<>();
            Map<String, String> innerConfDataMd5Map = new HashMap<>();
            for (String key : keys) {
                ConfDataDTO confDataDTO = dataConfCacheHelper.get(env, appname, key);
                if (confDataDTO == null) {
                    confDataDTO = ConfDataDTO.builder()
                            .env(env)
                            .appname(appname)
                            .key(key)
                            .value("")
                            .md5("")
                            .build();
                }
                innerConfDataMd5Map.put(key, confDataDTO.getMd5());

                if (Boolean.TRUE.equals(req.getQueryDetail())) {
                    innerConfDataMap.put(key, confDataDTO);
                }
            }

            confDataMd5Map.put(appname, innerConfDataMd5Map);
            confDataMap.put(appname, innerConfDataMap);
        });
        return OpenApiResp.ok(OpenDataConfQueryResp.builder()
                .confDataMd5Map(confDataMd5Map)
                .confDataMap(confDataMap)
                .env(env)
                .build());
    }

    /**
     * 订阅配置
     * @param req
     * @return
     */
    @PostMapping("/subscribe")
    public DeferredResult<OpenApiResp<PushClientEnvAppDTO>> subscribe(@RequestBody OpenDataConfQueryReq req) {
        DeferredResult<OpenApiResp<PushClientEnvAppDTO>> deferredResult = new DeferredResult<>(30 * 1000L, OpenApiResp.error("1001", "Subscribe timeout"));
        String env = req.getEnv();
        req.getAppKeys().forEach((appname, keys) -> {
            deferredResultHandler.addDeferredResult(env, appname, deferredResult);
        });
        return deferredResult;
    }
}

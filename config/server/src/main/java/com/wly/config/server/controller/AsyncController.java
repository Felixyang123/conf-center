package com.wly.config.server.controller;

import com.wly.config.server.service.HeavyLiftingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/api")
@Slf4j
public class AsyncController {

    // 模拟耗时服务
    @Autowired
    private HeavyLiftingService heavyLiftingService;

    // 使用DeferredResult实现异步处理
    @GetMapping("/async-deferred")
    public DeferredResult<ResponseEntity<String>> asyncDeferred() {
        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>(1000L);
        long cur = System.currentTimeMillis();
        // 设置超时处理
        deferredResult.onTimeout(() ->
                deferredResult.setErrorResult(
                        ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                                .body("Request timeout")
                )
        );

        // 异步处理任务
        heavyLiftingService.processHeavyTask(deferredResult);
        log.info("Async task started in {}ms", System.currentTimeMillis() - cur);
        return deferredResult;
    }

}
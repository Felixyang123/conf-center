package com.wly.config.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Service
@Slf4j
public class HeavyLiftingService {

    // 模拟耗时操作
    @Async("asyncTaskExecutor") // 使用指定的线程池
    public void processHeavyTask(DeferredResult deferredResult) {
        log.info("Start processing heavy task...");
        try {
            // 模拟耗时操作，如数据库查询、外部API调用等
            Thread.sleep(2000);
            deferredResult.setResult("Heavy task completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Task interrupted", e);
        }
        log.info("Heavy task completed");
    }
}
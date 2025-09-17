package com.wly.config.server.helper;

import com.wly.config.server.helper.bean.PushClientEnvAppDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class DeferredResultHandler implements SmartLifecycle {
    private final ConcurrentMap<String, CopyOnWriteArrayList<DeferredResult<Object>>> deferredResultsCache = new ConcurrentHashMap<>();

    private final AtomicBoolean running = new AtomicBoolean(false);

    @Value("${deferred.flush.interval:5000}")
    private Integer flushInterval;

    @Override
    public void start() {
        running.set(true);

        Thread deferredTasksFlushThread = new Thread(() -> {
            while (running.get()) {
                for (String key : deferredResultsCache.keySet()) {
                    CopyOnWriteArrayList<DeferredResult<Object>> deferredResults = deferredResultsCache.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>());
                    deferredResults.removeIf(DeferredResult::isSetOrExpired);
                }

                try {
                    Thread.sleep(flushInterval);
                } catch (InterruptedException e) {
                    log.warn("Thread interrupted");
                    if (Thread.interrupted()) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        deferredTasksFlushThread.setName("deferred-tasks-flush-thread");
        deferredTasksFlushThread.setDaemon(true);
        deferredTasksFlushThread.start();
        log.debug("DeferredResultHandler started");
    }

    @Override
    public void stop() {
        running.set(false);
        log.debug("DeferredResultHandler stopped");
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    private String buildCacheKey(String env, String appname) {
        return env + ":" + appname;
    }

    public void addDeferredResult(String env, String appname, DeferredResult deferredResult) {
        deferredResultsCache.computeIfAbsent(buildCacheKey(env, appname), k -> new CopyOnWriteArrayList<>()).add(deferredResult);
    }

    public void pushClient(String env, String appname) {
        String cacheKey = buildCacheKey(env, appname);
        log.debug("Push client: {}", cacheKey);
        CopyOnWriteArrayList<DeferredResult<Object>> deferredResults = deferredResultsCache.remove(cacheKey);
        if (deferredResults != null) {
            for (DeferredResult<Object> deferredResult : deferredResults) {
                deferredResult.setResult(new PushClientEnvAppDTO(env, appname));
            }
        }
    }
}

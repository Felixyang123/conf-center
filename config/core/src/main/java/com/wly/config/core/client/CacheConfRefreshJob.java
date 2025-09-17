package com.wly.config.core.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;

import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@Slf4j
public class CacheConfRefreshJob implements SmartLifecycle {
    private final CacheConfClient client;

    private final AtomicBoolean running = new AtomicBoolean(false);

    @Override
    public void start() {
        running.set(true);

        Thread refreshThread = new Thread(() -> {
            while (running.get()) {
                log.info("refresh cache conf");
                client.refresh();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.warn("Thread interrupted");
                    if (Thread.interrupted()) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        refreshThread.setDaemon(true);
        refreshThread.setName("config-refresh-thread");
        refreshThread.start();
    }

    @Override
    public void stop() {
        running.set(false);
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}

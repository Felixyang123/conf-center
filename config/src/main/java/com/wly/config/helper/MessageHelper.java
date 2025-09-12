package com.wly.config.helper;

import com.wly.config.dao.entity.Message;
import com.wly.config.helper.bean.ConfDataMessage;
import com.wly.config.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@Component
@Slf4j
public class MessageHelper implements SmartLifecycle {
    private final MessageService messageService;

    private final DataConfCacheHelper dataConfCacheHelper;

    private final AtomicBoolean running = new AtomicBoolean(false);

    @Value("${message.query.limit:5000}")
    private Integer queryLimit;

    @Value("${message.flush.interval:5000}")
    private Integer flushInterval;


    @Override
    public void start() {
        running.set(true);

        Thread messageFlushThread = new Thread(() -> {
            while (running.get()) {
                long offset = 1;
                List<Message> messages = messageService.queryFromOffset(offset, queryLimit);

                List<ConfDataMessage> confDataMessages = messages.stream().map(ConfDataMessage::buildFromMessage).toList();
                dataConfCacheHelper.checkAndPush(confDataMessages);

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
        messageFlushThread.setName("message-flush-thread");
        messageFlushThread.setDaemon(true);
        messageFlushThread.start();
        log.debug("MessageHelper started");
    }

    @Override
    public void stop() {
        running.set(false);
        log.debug("MessageHelper stopped");
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}

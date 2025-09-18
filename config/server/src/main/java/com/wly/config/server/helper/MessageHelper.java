package com.wly.config.server.helper;

import com.wly.config.server.dao.entity.Message;
import com.wly.config.server.helper.bean.ConfDataMessage;
import com.wly.config.server.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
            long offset = 1;
            while (running.get()) {
                List<Message> messages = messageService.queryFromOffset(offset, queryLimit);

                while (!CollectionUtils.isEmpty(messages)) {
                    List<ConfDataMessage> confDataMessages = messages.stream().map(ConfDataMessage::buildFromMessage).toList();
                    dataConfCacheHelper.checkAndPush(confDataMessages);
                    offset = messages.getLast().getId() + 1;
                    messages = messageService.queryFromOffset(offset, queryLimit);
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

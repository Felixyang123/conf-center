package com.wly.config.server.helper;

import com.wly.config.server.dao.entity.ConfData;
import com.wly.config.server.helper.bean.ConfDataDTO;
import com.wly.config.server.helper.bean.ConfDataMessage;
import com.wly.config.server.service.ConfDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@Component
@Slf4j
public class DataConfCacheHelper implements SmartLifecycle {
    private final ConfDataService confDataService;

    private final DeferredResultHandler deferredResultHandler;

    private volatile ConcurrentMap<String, ConfDataDTO> confDataCache = new ConcurrentHashMap<>();

    private final AtomicBoolean running = new AtomicBoolean(false);

    @Value("${dataconf.flush.interval:5000}")
    private Integer flushInterval;


    @Override
    public void start() {
        running.set(true);

        Thread dataConfFlushThread = new Thread(() -> {
            while (running.get()) {
                List<ConfData> envApps = confDataService.queryEnvAndAppname();
                if (!CollectionUtils.isEmpty(envApps)) {
                    ConcurrentMap<String, ConfDataDTO> confDataCacheNew = new ConcurrentHashMap<>();
                    for (ConfData envApp : envApps) {
                        List<ConfData> confDataListFromDB = confDataService.queryByEnvAndAppname(envApp.getEnv(), envApp.getAppname());
                        for (ConfData conf : confDataListFromDB) {
                            ConfDataDTO confDataDTO = ConfDataDTO.buildFromConfData(conf);
                            String cacheKey = buildCacheKey(conf.getEnv(), conf.getAppname(), conf.getKey());
                            confDataCacheNew.put(cacheKey, confDataDTO);
                        }
                    }

                    List<ConfDataDTO> confDataDiff = new ArrayList<>();
                    for (String key : confDataCacheNew.keySet()) {
                        ConfDataDTO confDataDTOOld = confDataCache.get(key);
                        ConfDataDTO confDataDTONew = confDataCacheNew.get(key);
                        if (confDataDTOOld == null || !confDataDTOOld.getMd5().equals(confDataDTONew.getMd5())) {
                            confDataDiff.add(confDataDTONew);
                        }
                    }

                    pushClient(confDataDiff);
                    this.confDataCache = confDataCacheNew;
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
        dataConfFlushThread.setName("data-conf-flush-thread");
        dataConfFlushThread.setDaemon(true);
        dataConfFlushThread.start();
        log.debug("DataConfCacheHelper started");
    }

    private void pushClient(List<ConfDataDTO> confDataDTOs) {
        for (ConfDataDTO confDataDTO : confDataDTOs) {
            deferredResultHandler.pushClient(confDataDTO.getEnv(), confDataDTO.getAppname());
        }
    }

    private String buildCacheKey(String env, String appname, String key) {
        return env + ":" + appname + ":" + key;
    }

    @Override
    public void stop() {
        running.set(false);
        log.debug("DataConfCacheHelper stopped");
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    public void checkAndPush(List<ConfDataMessage> confDataMessages) {
        List<ConfDataDTO> confDataDTOs = new ArrayList<>();
        for (ConfDataMessage message : confDataMessages) {
            ConfData confDataNew = confDataService.getByEnvAndAppnameAndKey(message.getEnv(), message.getAppname(), message.getKey());
            String cacheKey = buildCacheKey(message.getEnv(), message.getAppname(), message.getKey());
            ConfDataDTO confDataDTONew = ConfDataDTO.buildFromConfData(confDataNew);
            if (confDataCache.get(cacheKey) == null || !confDataCache.get(cacheKey).getMd5().equals(confDataDTONew.getMd5())) {
                this.confDataCache.put(cacheKey, confDataDTONew);
                confDataDTOs.add(confDataDTONew);
            }
        }

        pushClient(confDataDTOs);
    }

    public ConfDataDTO get(String env, String appname, String key) {
        return confDataCache.get(buildCacheKey(env, appname, key));
    }
}

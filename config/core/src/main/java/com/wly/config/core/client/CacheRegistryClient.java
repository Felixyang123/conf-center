package com.wly.config.core.client;

import com.wly.config.core.bean.pojo.InstanceDTO;
import com.wly.config.core.bean.pojo.req.OpenInstanceDiscoveryReq;
import com.wly.config.core.bean.pojo.resp.OpenInstanceDiscoveryResp;
import com.wly.config.core.config.RegistryClientProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
public class CacheRegistryClient implements SmartLifecycle {
    private final ConcurrentMap<String, List<InstanceDTO>> instancesCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, String> instancesMd5Cache = new ConcurrentHashMap<>();
    private final AtomicBoolean running = new AtomicBoolean(false);

    private final RegistryClientProps props;
    private final RegistryClient registryClient;

    public List<InstanceDTO> getInstances(String appname) {
        return instancesCache.computeIfAbsent(appname, k -> {
            OpenInstanceDiscoveryReq req = new OpenInstanceDiscoveryReq();
            req.setEnv(props.getEnv());
            req.setAppnames(List.of(appname));
            req.setAccessToken(props.getAccessToken());
            OpenInstanceDiscoveryResp discoveryResp = registryClient.discovery(props.parseServerAddress(), req);
            return Optional.ofNullable(discoveryResp.getInstances().get(appname)).orElse(new ArrayList<>());
        });
    }

    @Override
    public void start() {
        running.set(true);
        Thread refreshThread = new Thread(() -> {
            while (running.get()) {
                if (!instancesCache.isEmpty()) {
                    OpenInstanceDiscoveryReq req = new OpenInstanceDiscoveryReq();
                    req.setEnv(props.getEnv());
                    req.setAppnames(new ArrayList<>(instancesCache.keySet()));
                    req.setAccessToken(props.getAccessToken());
                    OpenInstanceDiscoveryResp discoveryResp = registryClient.discovery(props.parseServerAddress(), req);
                    if (discoveryResp == null || discoveryResp.getInstances() == null) {
                        instancesCache.clear();
                        instancesMd5Cache.clear();
                    } else {
                        discoveryResp.getInstances().forEach((appname, instances) -> {
                            instancesCache.put(appname, instances);
                            instancesMd5Cache.put(appname, discoveryResp.getInstancesMd5().get(appname));
                        });
                        // 清空远程已经注销了的实例对应的本地缓存中的信息，不直接删除是为了防止缓存击穿
                        instancesCache.entrySet().stream().filter(entry -> !discoveryResp.getInstances().containsKey(entry.getKey()))
                                .forEach(entry -> entry.getValue().clear());
                    }
                }

                log.debug("Instance refresh end...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.warn("Instance refresh thread interrupted");
                    if (Thread.interrupted()) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        refreshThread.setName("instance-refresh-thread");
        refreshThread.setDaemon(true);
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

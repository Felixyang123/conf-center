package com.wly.config.core.client;

import com.wly.config.core.bean.pojo.InstanceDTO;
import com.wly.config.core.bean.pojo.req.OpenInstanceRegisterReq;
import com.wly.config.core.config.RegistryClientProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class RegistryHelper implements SmartLifecycle {
    private final CacheRegistryClient cacheRegistryClient;
    private final RegistryClient registryClient;
    private final RegistryClientProps props;
    private ScheduledExecutorService scheduledExecutorService;

    private final ConcurrentMap<String, OpenInstanceRegisterReq> renewTaskMap = new ConcurrentHashMap<>();

    public RegistryHelper(CacheRegistryClient cacheRegistryClient, RegistryClient registryClient, RegistryClientProps props) {
        this.cacheRegistryClient = cacheRegistryClient;
        this.registryClient = registryClient;
        this.props = props;
        initScheduledTask();
    }

    public List<InstanceDTO> get(String appname) {
        return cacheRegistryClient.getInstances(appname);
    }

    public void register(RegistryClientProps props) {
        OpenInstanceRegisterReq req = new OpenInstanceRegisterReq();
        req.setEnv(props.getEnv());
        req.setAccessToken(props.getAccessToken());
        req.setAppname(props.getAppname());
        req.setIp(props.getHost());
        req.setPort(props.getPort());
        req.setExt(props.getExt());
        req.setHeartbeatInterval(props.getHeartbeatInterval());
        registryClient.register(props.parseServerAddress(), req);
        renewTaskMap.put(props.getAppname(), req);
    }

    /**
     * 初始化定时续租任务
     */
    private void initScheduledTask() {
        // 创建单线程的定时调度线程池
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "registry-renew-thread");
            thread.setDaemon(true);
            return thread;
        });

        // 安排定时任务，每30秒执行一次续租
        this.scheduledExecutorService.scheduleAtFixedRate(
                () -> {
                    for (OpenInstanceRegisterReq req : renewTaskMap.values()) {
                        try {
                            registryClient.register(props.parseServerAddress(), req);
                        } catch (Exception e) {
                            log.error("Renew instance failed, task: {}, ", req, e);
                        }
                    }

                },
                30,  // 初始延迟30秒
                30,  // 每30秒执行一次
                TimeUnit.SECONDS
        );
    }

    @Override
    public void start() {
        register(props);
    }

    @Override
    public void stop() {
        shutdownExecutor();

        unregisterInstance();
    }

    private void unregisterInstance() {
        for (OpenInstanceRegisterReq req : renewTaskMap.values()) {
            try {
                registryClient.unregister(props.parseServerAddress(), req);
            } catch (Exception e) {
                log.error("Unregister instance failed, task: {}, ", req, e);
            }
        }
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    public void shutdownExecutor() {
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
            try {
                if (!scheduledExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduledExecutorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduledExecutorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}

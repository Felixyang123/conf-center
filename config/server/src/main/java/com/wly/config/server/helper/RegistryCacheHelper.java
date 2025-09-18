package com.wly.config.server.helper;

import com.alibaba.fastjson2.JSON;
import com.wly.config.server.dao.entity.ConfInstance;
import com.wly.config.server.dao.rep.ConfInstanceRepository;
import com.wly.config.server.helper.bean.InstanceDTO;
import com.wly.config.server.helper.bean.InstanceMessage;
import com.wly.config.server.utils.DigestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@RequiredArgsConstructor
public class RegistryCacheHelper implements SmartLifecycle {
    protected final ConcurrentMap<String, List<InstanceDTO>> instanceCache = new ConcurrentHashMap<>();
    protected final ConcurrentMap<String, String> instanceCacheMD5 = new ConcurrentHashMap<>();

    private final ConfInstanceRepository instanceRepository;
    private final DeferredResultHandler deferredResultHandler;

    @Override
    public void start() {
        List<ConfInstance> envAndAppnames = instanceRepository.queryEnvAndAppnames();
        for (ConfInstance envAndAppname : envAndAppnames) {
            List<ConfInstance> confInstancesDB = instanceRepository.queryByEnvAndAppname(envAndAppname.getEnv(), envAndAppname.getAppname());
            String key = buildCacheKey(envAndAppname.getEnv(), envAndAppname.getAppname());
            List<InstanceDTO> instanceDTOS = instanceCache.computeIfAbsent(key, k -> confInstancesDB.stream().map(InstanceDTO::buildFromInstance).toList());
            instanceCacheMD5.put(key, md5(instanceDTOS));
        }
        log.info("init instance cache success, size: {}", instanceCache.size());
    }

    private String buildCacheKey(String env, String appname) {
        return env + ":" + appname;
    }

    private String md5(List<InstanceDTO> instanceDTOS) {
        instanceDTOS.sort(Comparator.comparing(instanceDTO -> instanceDTO.getIp() + ":" + instanceDTO.getPort()));
        return DigestUtils.md5(JSON.toJSONString(instanceDTOS));
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    public void checkAndPush(List<InstanceMessage> instanceMessages) {
        List<InstanceMessage> changedInstances = new ArrayList<>();
        for (InstanceMessage instanceMessage : instanceMessages) {
            List<ConfInstance> confInstancesDB = instanceRepository.queryByEnvAndAppname(instanceMessage.getEnv(), instanceMessage.getAppname());
            String key = buildCacheKey(instanceMessage.getEnv(), instanceMessage.getAppname());

            if (CollectionUtils.isEmpty(confInstancesDB)) {
                instanceCache.remove(key);
                instanceCacheMD5.remove(key);
                changedInstances.add(instanceMessage);
                continue;
            }

            List<InstanceDTO> instanceDTOS = confInstancesDB.stream().map(InstanceDTO::buildFromInstance).toList();
            String md5 = md5(instanceDTOS);
            if (!instanceCacheMD5.containsKey(key) || !instanceCacheMD5.get(key).equals(md5)) {
                instanceCache.put(key, instanceDTOS);
                instanceCacheMD5.put(key, md5);
                changedInstances.add(instanceMessage);
            }
        }

        // 通知客户端
        for (InstanceMessage changedInstance : changedInstances) {
            deferredResultHandler.pushClient(changedInstance.getEnv(), changedInstance.getAppname());
        }
    }
}

package com.wly.config.core.client;

import com.wly.config.core.bean.ConfDataDTO;
import com.wly.config.core.bean.pojo.resp.OpenDataConfQueryResp;
import com.wly.config.core.config.ConfClientProps;
import com.wly.config.core.listener.ConfListenerRegistrar;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CacheConfClient {
    private final ConcurrentMap<String, ConcurrentMap<String, ConfDataDTO>> cacheConfDataMap = new ConcurrentHashMap<>();

    private final ConfClient confClient;
    private final ConfClientProps confClientProps;
    private final ConfListenerRegistrar confListenerRegistrar;

    public String get(String appname, String key, String defaultValue) {
        ConcurrentMap<String, ConfDataDTO> appConfs = cacheConfDataMap.computeIfAbsent(appname, k -> new ConcurrentHashMap<>());

        ConfDataDTO confData = appConfs.computeIfAbsent(key, k -> {
            Map<String, List<String>> confKeys = Map.of(appname, List.of(key));
            OpenDataConfQueryResp confQueryResp = confClient.query(confClientProps.parseServerAddress(), confClientProps.getAccessToken(), confClientProps.getEnv(), confKeys);
            if (confQueryResp != null) {
                Map<String, ConfDataDTO> remoteAppConfs = confQueryResp.getConfDataMap().get(appname);
                if (remoteAppConfs != null && remoteAppConfs.containsKey(key)) {
                    return remoteAppConfs.get(key);
                }
            }
            return ConfDataDTO.builder().env(confClientProps.getEnv()).appname(appname).key(key).value(null).md5(null).build();
        });
        return Optional.ofNullable(confData.getValue()).orElse(defaultValue);
    }


    public void refresh() {
        Map<String, List<String>> confKeys = cacheConfDataMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> new ArrayList<>(entry.getValue().keySet())));

        if (CollectionUtils.isEmpty(confKeys)) {
            return;
        }

        OpenDataConfQueryResp confQueryResp = confClient.query(confClientProps.parseServerAddress(), confClientProps.getAccessToken(), confClientProps.getEnv(), confKeys);

        if (confQueryResp == null || confQueryResp.getConfDataMap() == null) {
            return;
        }

        List<ConfDataDTO> changedConfDatas = new ArrayList<>();
        // 刷新缓存
        refreshCache(confQueryResp, changedConfDatas);

        // notify listeners
        notifyChange(changedConfDatas);

        // 清除缓存中在服务端已删除的keys
        resetInvalidKeys(confKeys, confQueryResp);
    }

    private void refreshCache(OpenDataConfQueryResp confQueryResp, List<ConfDataDTO> changedConfDatas) {
        confQueryResp.getConfDataMap().forEach((appname, confDataMap) -> {
            ConcurrentMap<String, ConfDataDTO> oldConfDataMap = cacheConfDataMap.computeIfAbsent(appname, k -> new ConcurrentHashMap<>());

            confDataMap.forEach((key, confDataDTO) -> {
                if (!oldConfDataMap.containsKey(key) || !Objects.equals(confDataDTO.getMd5(), oldConfDataMap.get(key).getMd5())) {
                    oldConfDataMap.put(key, confDataDTO);
                    changedConfDatas.add(confDataDTO);
                }
            });
        });
    }

    private void resetInvalidKeys(Map<String, List<String>> confKeys, OpenDataConfQueryResp confQueryResp) {
        confKeys.forEach((appname, keys) -> {
            Map<String, ConfDataDTO> newConfDataMap = confQueryResp.getConfDataMap().get(appname);
            ConcurrentMap<String, ConfDataDTO> oldAppConfDataMap = cacheConfDataMap.get(appname);
            if (newConfDataMap == null) {
                oldAppConfDataMap.values().forEach(conf -> {
                    conf.setValue(null);
                    conf.setMd5(null);
                    confListenerRegistrar.notifyChange(appname, conf.getKey(), null);
                });
            } else {
                oldAppConfDataMap.values().stream()
                        .filter(conf -> !newConfDataMap.containsKey(conf.getKey()))
                        .forEach(conf -> {
                            conf.setValue(null);
                            conf.setMd5(null);
                            confListenerRegistrar.notifyChange(appname, conf.getKey(), null);
                        });
            }
        });
    }

    private void notifyChange(List<ConfDataDTO> changedConfDatas) {
        for (ConfDataDTO changedConfData : changedConfDatas) {
            confListenerRegistrar.notifyChange(changedConfData.getAppname(), changedConfData.getKey(), changedConfData.getValue());
        }
    }

}

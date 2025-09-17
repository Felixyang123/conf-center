package com.wly.config.core.client;

import com.wly.config.core.bean.ConfDataDTO;
import com.wly.config.core.bean.pojo.resp.OpenDataConfQueryResp;
import com.wly.config.core.config.ConfClientProps;
import com.wly.config.core.listener.ConfListenerRegistrar;
import com.wly.config.core.utils.DigestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CacheConfClient {
    private final ConcurrentMap<String, ConcurrentMap<String, String>> cacheConfDataMd5Map = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, ConcurrentMap<String, String>> cacheConfDataMap = new ConcurrentHashMap<>();

    private final ConfClient confClient;
    private final ConfClientProps confClientProps;
    private final ConfListenerRegistrar confListenerRegistrar;

    public String get(String appname, String key, String defaultValue) {
        ConcurrentMap<String, String> appConfs = cacheConfDataMap.computeIfAbsent(appname, k -> new ConcurrentHashMap<>());
        ConcurrentMap<String, String> appConfsMd5 = cacheConfDataMd5Map.computeIfAbsent(appname, k -> new ConcurrentHashMap<>());

        return appConfs.computeIfAbsent(key, k -> {
            Map<String, List<String>> confKeys = Map.of(appname, List.of(key));
            OpenDataConfQueryResp confQueryResp = confClient.query(confClientProps.parseServerAddress(), confClientProps.getAccessToken(), confClientProps.getEnv(), confKeys);
            if (confQueryResp != null) {
                Map<String, ConfDataDTO> remoteAppConfs = confQueryResp.getConfDataMap().get(appname);
                if (remoteAppConfs != null && remoteAppConfs.containsKey(key)) {
                    ConfDataDTO confDataDTO = remoteAppConfs.get(key);
                    appConfsMd5.put(key, confDataDTO.getMd5());
                    return confDataDTO.getValue();
                }
            }
            appConfsMd5.put(key, DigestUtils.md5(defaultValue));
            return defaultValue;
        });

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
        removeInvalidKeys(confKeys, confQueryResp);
    }

    private void refreshCache(OpenDataConfQueryResp confQueryResp, List<ConfDataDTO> changedConfDatas) {
        confQueryResp.getConfDataMap().forEach((appname, confDataMap) -> {
            ConcurrentMap<String, String> oldConfDataMap = cacheConfDataMap.computeIfAbsent(appname, k -> new ConcurrentHashMap<>());
            ConcurrentMap<String, String> oldConfDataMd5Map = cacheConfDataMd5Map.computeIfAbsent(appname, k -> new ConcurrentHashMap<>());

            confDataMap.forEach((key, confDataDTO) -> {
                if (!oldConfDataMd5Map.containsKey(key) || !oldConfDataMd5Map.get(key).equals(confDataDTO.getMd5())) {
                    oldConfDataMap.put(key, confDataDTO.getValue());
                    oldConfDataMd5Map.put(key, confDataDTO.getMd5());
                    changedConfDatas.add(confDataDTO);
                }
            });
        });
    }

    private void removeInvalidKeys(Map<String, List<String>> confKeys, OpenDataConfQueryResp confQueryResp) {
        confKeys.forEach((appname, keys) -> {
            Map<String, ConfDataDTO> newConfDataMap = confQueryResp.getConfDataMap().get(appname);
            if (newConfDataMap == null) {
                cacheConfDataMap.remove(appname);
            } else if (cacheConfDataMap.get(appname) != null) {
                cacheConfDataMap.get(appname).entrySet().removeIf(entry -> !newConfDataMap.containsKey(entry.getKey()));
            }
        });
    }

    private void notifyChange(List<ConfDataDTO> changedConfDatas) {
        for (ConfDataDTO changedConfData : changedConfDatas) {
            confListenerRegistrar.notify(changedConfData.getAppname(), changedConfData.getKey(), changedConfData.getValue());
        }
    }

    private String buildConfKey(String appname, String key) {
        return appname + ":" + key;
    }
}

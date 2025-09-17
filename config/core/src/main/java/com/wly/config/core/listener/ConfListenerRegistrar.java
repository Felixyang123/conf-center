package com.wly.config.core.listener;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConfListenerRegistrar {
    private final ConcurrentMap<String, ConfListener> listenerMap = new ConcurrentHashMap<>();

    public ConfListenerRegistrar(List<ConfListener> listeners) {
        register(listeners);
    }

    public void register(List<ConfListener> listeners) {
        for (ConfListener listener : listeners) {
            listenerMap.put(listener.name(), listener);
        }
    }

    public ConfListener get(String name) {
        return listenerMap.get(name);
    }

    public void notify(String appname, String key, String value) {
        String confKey = buildConfKey(appname, key);
        ConfListener listener = get(confKey);
        if (listener != null) {
            listener.onChange(appname, key, value);
        }
    }

    private String buildConfKey(String appname, String key) {
        return appname + ":" + key;
    }

}

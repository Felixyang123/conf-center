package com.wly.config.core.listener;

public interface ConfListener {
    void onChange(String appname, String key, String value);

    String name();
}

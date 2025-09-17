package com.wly.config.core.client;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ConfHelper implements ApplicationContextAware {
    private static CacheConfClient cacheConfClient;

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ConfHelper.applicationContext = applicationContext;
    }

    public static String get(String appname, String key, String defaultValue) {
        return cacheConfClient.get(appname, key, defaultValue);
    }
}

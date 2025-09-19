package com.wly.config.core.client;

import com.wly.config.core.bean.pojo.InstanceDTO;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

public class RegistryHelper implements ApplicationContextAware {

    private static CacheRegistryClient registryClient;

    public static List<InstanceDTO> get(String appname) {
        return registryClient.getInstances(appname);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RegistryHelper.registryClient = applicationContext.getBean(CacheRegistryClient.class);
    }
}

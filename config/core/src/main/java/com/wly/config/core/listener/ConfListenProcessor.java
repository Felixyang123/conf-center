package com.wly.config.core.listener;

import com.wly.config.core.client.ConfHelper;
import com.wly.config.core.annotation.ConfListen;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.util.List;

public record ConfListenProcessor(ConfListenerRegistrar confListenerRegistrar,
                                  ApplicationContext applicationContext) implements SmartInstantiationAwareBeanPostProcessor {
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            ConfListen confListen = field.getAnnotation(ConfListen.class);
            if (confListen != null) {
                String value = ConfHelper.get(confListen.appname(), confListen.key(), confListen.defaultValue());
                SpringConfAnnotationListener.reflectSetValue(bean, field.getName(), value);
                if (confListen.listen()) {
                    SpringConfAnnotationListener confAnnotationListener = new SpringConfAnnotationListener(applicationContext, confListen.appname(), confListen.key());
                    confAnnotationListener.addBeanNameField(beanName, field.getName());
                    confListenerRegistrar.register(List.of(confAnnotationListener));
                }
            }
        });
        return true;
    }
}

package com.wly.config.core.listener;

import com.wly.config.core.bean.BeanNameField;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpringConfAnnotationListener implements ConfListener {
    private final CopyOnWriteArrayList<BeanNameField> beanNameFields = new CopyOnWriteArrayList<>();

    private static ApplicationContext applicationContext;

    private final String appname;

    private final String key;

    public SpringConfAnnotationListener(ApplicationContext applicationContext, String appname, String key) {
        SpringConfAnnotationListener.applicationContext = applicationContext;
        this.appname = appname;
        this.key = key;
    }

    @Override
    public void onChange(String appname, String key, String value) {
        for (BeanNameField beanNameField : beanNameFields) {
            reflectSetValue(beanNameField, value);
        }
    }

    @Override
    public String name() {
        return appname + ":" + key;
    }

    public void addBeanNameField(String beanName, String fieldName) {
        beanNameFields.add(new BeanNameField(beanName, fieldName));
    }

    public static void reflectSetValue(BeanNameField beanNameField, Object value) {
        Object bean = applicationContext.getBean(beanNameField.getBeanName());
        reflectSetValue(bean, beanNameField.getFieldName(), value);
    }

    public static void reflectSetValue(Object bean, String fieldName, Object value) {
        Field field;
        if (AopUtils.isAopProxy(bean)) {
            field = ReflectionUtils.findField(AopUtils.getTargetClass(bean), fieldName);
        } else {
            field = ReflectionUtils.findField(bean.getClass(), fieldName);
        }

        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, bean, value);
        }
    }
}

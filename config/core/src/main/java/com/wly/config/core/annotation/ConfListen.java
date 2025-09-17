package com.wly.config.core.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ConfListen {

    String appname();

    String key();

    String defaultValue() default "";

    boolean listen() default true;
}

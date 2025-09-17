package com.wly.config.core.annotation;

import com.wly.config.core.config.ConfClientConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Import({ConfClientConfiguration.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface EnableConf {
}

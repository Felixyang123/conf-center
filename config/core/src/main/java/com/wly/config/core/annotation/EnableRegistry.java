package com.wly.config.core.annotation;

import com.wly.config.core.config.RegistryClientConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Import({RegistryClientConfiguration.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface EnableRegistry {
}

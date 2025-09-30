package com.wly.config.server.token;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessTokenCheck {
    
    /**
     * 固定token值，优先级高于expression
     */
    String token() default "";

    String app() default "";
    
    /**
     * SPEL表达式，用于从方法参数中提取token
     */
    String tokenExpr() default "";

    String appExpr() default "";

    String env() default "";

    String envExpr() default "";
}
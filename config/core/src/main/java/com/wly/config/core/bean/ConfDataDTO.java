package com.wly.config.core.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfDataDTO {
    /**
     * Env（环境唯一标识）
     */
    private String env;

    /**
     * AppName（服务唯一标识）
     */
    private String appname;

    /**
     * 配置项Key
     */
    private String key;

    /**
     * 配置项Value
     */
    private String value;

    private String md5;
}

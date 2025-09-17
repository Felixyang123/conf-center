package com.wly.config.server.helper.bean;

import com.wly.config.server.dao.entity.ConfData;
import com.wly.config.server.utils.DigestUtils;
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


    public static ConfDataDTO buildFromConfData(ConfData confData) {
        return ConfDataDTO.builder()
                .env(confData.getEnv())
                .appname(confData.getAppname())
                .key(confData.getKey())
                .value(confData.getValue())
                .md5(DigestUtils.md5(confData.getValue()))
                .build();
    }
}

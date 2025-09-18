package com.wly.config.server.helper.bean;

import com.wly.config.server.dao.entity.ConfInstance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstanceDTO {

    private String env;

    private String appname;

    private String ip;

    private String port;

    private String ext;

    public static InstanceDTO buildFromInstance(ConfInstance instance) {
        return InstanceDTO.builder()
                .env(instance.getEnv())
                .appname(instance.getAppname())
                .ip(instance.getIp())
                .port(instance.getPort())
                .ext(instance.getExt())
                .build();
    }
}

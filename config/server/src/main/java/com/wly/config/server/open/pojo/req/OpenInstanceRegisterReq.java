package com.wly.config.server.open.pojo.req;

import com.wly.config.server.dao.entity.ConfInstance;
import com.wly.config.server.open.pojo.OpenApiReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class OpenInstanceRegisterReq extends OpenApiReq {
    @Serial
    private static final long serialVersionUID = 3089521705642862252L;

    private String appname;

    private String ip;

    private String port;

    private String ext;

    public static ConfInstance parseInstance(OpenInstanceRegisterReq req) {
        return ConfInstance.builder()
                .env(req.getEnv())
                .appname(req.getAppname())
                .ip(req.getIp())
                .port(req.getPort())
                .ext(req.getExt())
                .status(ConfInstance.RUNNING)
                .build();
    }
}

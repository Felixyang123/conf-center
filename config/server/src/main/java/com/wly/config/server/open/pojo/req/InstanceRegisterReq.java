package com.wly.config.server.open.pojo.req;

import com.wly.config.server.dao.entity.ConfInstance;
import com.wly.config.server.open.pojo.OpenApiReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class InstanceRegisterReq extends OpenApiReq {
    @Serial
    private static final long serialVersionUID = 3089521705642862252L;

    private String appname;

    private String ip;

    private String port;

    private String ext;

    public static ConfInstance parseInstance(InstanceRegisterReq req) {
        Date date = new Date();
        return ConfInstance.builder()
                .env(req.getEnv())
                .appname(req.getAppname())
                .ip(req.getIp())
                .port(req.getPort())
                .ext(req.getExt())
                .status(0)
                .addTime(date)
                .updateTime(date)
                .build();
    }
}

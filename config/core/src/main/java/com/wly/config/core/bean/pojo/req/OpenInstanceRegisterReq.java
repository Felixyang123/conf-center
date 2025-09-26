package com.wly.config.core.bean.pojo.req;

import com.wly.config.core.bean.pojo.OpenApiReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class OpenInstanceRegisterReq extends OpenApiReq {
    @Serial
    private static final long serialVersionUID = 4041523626192452504L;

    private String appname;

    private String ip;

    private String port;

    private String ext;

    private Long heartbeatInterval;
}

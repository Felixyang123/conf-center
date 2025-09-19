package com.wly.config.server.open.pojo.req;

import com.wly.config.server.open.pojo.OpenApiReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OpenInstanceDiscoveryReq extends OpenApiReq {
    @Serial
    private static final long serialVersionUID = 645897969240971500L;

    private List<String> appnames;
}

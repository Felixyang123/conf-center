package com.wly.config.core.bean.pojo.req;

import com.wly.config.core.bean.pojo.OpenApiReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OpenInstanceDiscoveryReq extends OpenApiReq {
    @Serial
    private static final long serialVersionUID = -8382885068487968884L;

    private List<String> appnames;
}

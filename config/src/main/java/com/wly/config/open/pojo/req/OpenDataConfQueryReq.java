package com.wly.config.open.pojo.req;

import com.wly.config.open.pojo.OpenApiReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class OpenDataConfQueryReq extends OpenApiReq {
    @Serial
    private static final long serialVersionUID = -2714977310285438846L;

    /**
     * {
     * "appname1": ["k1", "k2"],
     * "appname2": ["k1", "k2"]
     * }
     */
    private Map<String, List<String>> appKeys;

    private Boolean queryDetail;
}

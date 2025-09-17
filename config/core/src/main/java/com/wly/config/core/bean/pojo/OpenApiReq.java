package com.wly.config.core.bean.pojo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class OpenApiReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 5458379736739199582L;
    private String accessToken;

    private String env;
}

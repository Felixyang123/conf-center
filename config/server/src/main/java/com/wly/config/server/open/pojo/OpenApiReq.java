package com.wly.config.server.open.pojo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class OpenApiReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 8811490603847074604L;

    private String accessToken;

    private String env;
}

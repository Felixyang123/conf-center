package com.wly.config.core.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushClientEnvAppDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6024645814019914925L;

    private String env;

    private String appname;
}

package com.wly.config.core.bean.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenApiResp<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 3716997885375752126L;

    private String code;

    private String message;

    private T data;
}

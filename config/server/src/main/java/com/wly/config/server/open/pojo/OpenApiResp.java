package com.wly.config.server.open.pojo;

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
    private static final long serialVersionUID = 4526122243582512663L;

    private String code;

    private String message;

    private T data;


    public static <T> OpenApiResp<T> ok() {
        return ok(null);
    }

    public static <T> OpenApiResp<T> ok(T data) {
        return new OpenApiResp<>("1", "ok", data);
    }

    public static <T> OpenApiResp<T> error(String code, String message) {
        return new OpenApiResp<>(code, message, null);
    }
}

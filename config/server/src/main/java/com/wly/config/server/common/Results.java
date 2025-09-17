package com.wly.config.server.common;

public class Results {

    public static <T> Result<T> ok(T data) {
        return new Result<>("1", "OK", data);
    }

    public static <T> Result<T> error(String code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }
}

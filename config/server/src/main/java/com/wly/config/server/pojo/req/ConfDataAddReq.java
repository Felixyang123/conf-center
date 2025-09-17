package com.wly.config.server.pojo.req;

import com.wly.config.server.dao.entity.ConfData;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class ConfDataAddReq implements Serializable {
    @Serial
    private static final long serialVersionUID = -5328799527432925670L;

    /**
     * Env（环境唯一标识）
     */
    private String env;

    /**
     * AppName（服务唯一标识）
     */
    private String appname;

    /**
     * 配置项Key
     */
    private String key;

    /**
     * 配置项Value
     */
    private String value;

    /**
     * 配置项描述
     */
    private String desc;

    public static ConfData transfer(ConfDataAddReq req) {
        Date now = new Date();
        return ConfData.builder()
                .env(req.env)
                .appname(req.appname)
                .key(req.key)
                .value(req.value)
                .desc(req.desc)
                .addTime(now)
                .updateTime(now)
                .build();
    }
}

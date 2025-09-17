package com.wly.config.server.pojo.req;

import com.wly.config.server.dao.entity.ConfData;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class ConfDataEditReq implements Serializable {
    @Serial
    private static final long serialVersionUID = -7307543503892812303L;

    private Long id;

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

    public static ConfData transfer(ConfDataEditReq req) {
        Date now = new Date();
        return ConfData.builder()
                .id(req.getId())
                .key(req.getKey())
                .value(req.getValue())
                .desc(req.getDesc())
                .updateTime(now)
                .build();
    }
}

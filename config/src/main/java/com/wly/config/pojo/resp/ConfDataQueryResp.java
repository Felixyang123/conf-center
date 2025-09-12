package com.wly.config.pojo.resp;

import com.wly.config.dao.entity.ConfData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfDataQueryResp implements Serializable {
    @Serial
    private static final long serialVersionUID = 684433614809515160L;

    /**
     * id
     */
    private long id;

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

    /**
     * 新增时间
     */
    private Date addTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public static ConfDataQueryResp buildFromConfData(ConfData confData) {
        return ConfDataQueryResp.builder()
                .id(confData.getId())
                .env(confData.getEnv())
                .appname(confData.getAppname())
                .key(confData.getKey())
                .value(confData.getValue())
                .desc(confData.getDesc())
                .addTime(confData.getAddTime())
                .updateTime(confData.getUpdateTime())
                .build();
    }
}

package com.wly.config.server.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "conf_instance", autoResultMap = true)
public class ConfInstance {
    public static final Integer RUNNING = 0;
    public static final Integer SHOUTDOWN = 1;

    private Long id;

    private String env;

    private String appname;

    private String ip;

    private String port;

    private String ext;

    /**
     * 0: 运行中 1: 停止
     */
    private Integer status;

    private Long expireTime;

    private Date addTime;

    private Date updateTime;

}

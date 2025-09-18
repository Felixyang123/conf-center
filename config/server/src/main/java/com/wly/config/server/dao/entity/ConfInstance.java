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

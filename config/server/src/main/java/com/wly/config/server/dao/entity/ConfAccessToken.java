package com.wly.config.server.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName(value = "conf_access_token", autoResultMap = true)
public class ConfAccessToken {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String appname;

    private String env;

    private String accessToken;

    /**
     * 0: 正常 1: 禁用
     */
    private Integer status;

    private Date addTime;

    private Date updateTime;
}

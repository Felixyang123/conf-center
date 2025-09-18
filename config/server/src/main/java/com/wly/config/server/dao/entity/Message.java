package com.wly.config.server.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
*  Message Entity
*
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "conf_message", autoResultMap = true)
public class Message implements Serializable {

    public static final Integer CONF_DATA = 1;
    public static final Integer CONF_INSTANCE = 0;

    @Serial
    private static final long serialVersionUID = -5866258044996022930L;
    /**
    * id
    */
    private Long id;

    /**
    * 消息类型：0-注册更新 1-配置更新
    */
    private Integer type;

    /**
    * 消息正文，json结构体
    */
    private String data;

    /**
    * 新增时间
    */
    private Date addTime;

    /**
    * 更新时间
    */
    private Date updateTime;

}
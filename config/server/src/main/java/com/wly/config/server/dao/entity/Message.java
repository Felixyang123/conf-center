package com.wly.config.server.dao.entity;

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
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = -5866258044996022930L;
    /**
    * id
    */
    private long id;

    /**
    * 消息类型：0-注册更新
    */
    private int type;

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
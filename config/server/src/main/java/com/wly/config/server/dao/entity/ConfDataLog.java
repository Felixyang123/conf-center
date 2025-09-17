package com.wly.config.server.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
*  ConfDataLog Entity
*
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfDataLog implements Serializable {

    @Serial
    private static final long serialVersionUID = -3728707941049848612L;
    /**
    * id
    */
    private long id;

    /**
    * 配置数据ID
    */
    private long dataId;

    /**
    * 历史数据，配置项Value
    */
    private String value;

    /**
    * 操作人，账号
    */
    private String optUsername;

    /**
    * 新增时间
    */
    private Date addTime;

    /**
    * 更新时间
    */
    private Date updateTime;

}
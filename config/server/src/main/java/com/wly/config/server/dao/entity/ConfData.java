package com.wly.config.server.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
*  ConfData Entity
*
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "conf_data", autoResultMap = true)
public class ConfData implements Serializable {

    @Serial
    private static final long serialVersionUID = 8635696909211291870L;
    /**
    * id
    */
    private Long id;

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
    @TableField("`key`")
    private String key;

    /**
    * 配置项Value
    */
    private String value;

    /**
    * 配置项描述
    */
    @TableField("`desc`")
    private String desc;

    /**
    * 新增时间
    */
    private Date addTime;

    /**
    * 更新时间
    */
    private Date updateTime;

}
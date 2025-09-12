package com.wly.config.helper.bean;

import com.alibaba.fastjson2.JSON;
import com.wly.config.dao.entity.ConfData;
import com.wly.config.dao.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfDataMessage {

    private String env;

    private String appname;

    private String key;

    public static ConfDataMessage buildFromMessage(Message message) {
        ConfData confData = JSON.parseObject(message.getData(), ConfData.class);
        return ConfDataMessage.builder().env(confData.getEnv()).appname(confData.getAppname()).key(confData.getKey()).build();
    }
}

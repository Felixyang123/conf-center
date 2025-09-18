package com.wly.config.server.helper.bean;

import com.alibaba.fastjson2.JSON;
import com.wly.config.server.dao.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InstanceMessage {

    private String env;

    private String appname;

    public static InstanceMessage parseFromMessage(Message message) {
        return JSON.parseObject(message.getData(), InstanceMessage.class);
    }
}

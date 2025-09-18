package com.wly.config.server.service;

import com.alibaba.fastjson2.JSON;
import com.wly.config.server.config.InstanceProps;
import com.wly.config.server.dao.entity.ConfInstance;
import com.wly.config.server.dao.entity.Message;
import com.wly.config.server.dao.rep.ConfInstanceRepository;
import com.wly.config.server.helper.MessageHelper;
import com.wly.config.server.helper.bean.InstanceMessage;
import com.wly.config.server.open.pojo.req.InstanceRegisterReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConfInstanceService {
    @Resource
    private ConfInstanceRepository confInstanceRepository;
    @Resource
    private InstanceProps instanceProps;
    @Resource
    private MessageHelper messageHelper;

    @Transactional(rollbackFor = Exception.class)
    public void register(InstanceRegisterReq req) {
        ConfInstance confInstance = InstanceRegisterReq.parseInstance(req);
        confInstance.setExpireTime(System.currentTimeMillis() + instanceProps.getHeartbeatInterval() * 3);
        confInstanceRepository.save(confInstance);

        Message message = Message.builder().type(Message.CONF_INSTANCE).data(JSON.toJSONString(new InstanceMessage(req.getEnv(), req.getAppname())))
                .addTime(confInstance.getAddTime()).updateTime(confInstance.getAddTime()).build();
        messageHelper.broadcast(List.of(message));
    }
}

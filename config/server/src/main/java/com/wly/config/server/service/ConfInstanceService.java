package com.wly.config.server.service;

import com.alibaba.fastjson2.JSON;
import com.wly.config.server.config.InstanceProps;
import com.wly.config.server.dao.entity.ConfInstance;
import com.wly.config.server.dao.entity.Message;
import com.wly.config.server.dao.rep.ConfInstanceRepository;
import com.wly.config.server.helper.DeferredResultHandler;
import com.wly.config.server.helper.MessageHelper;
import com.wly.config.server.helper.RegistryCacheHelper;
import com.wly.config.server.helper.bean.InstanceDTO;
import com.wly.config.server.helper.bean.InstanceMessage;
import com.wly.config.server.helper.bean.PushClientEnvAppDTO;
import com.wly.config.server.open.pojo.OpenApiResp;
import com.wly.config.server.open.pojo.req.OpenInstanceDiscoveryReq;
import com.wly.config.server.open.pojo.req.OpenInstanceRegisterReq;
import com.wly.config.server.open.pojo.resp.OpenInstanceDiscoveryResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfInstanceService {
    @Resource
    private ConfInstanceRepository confInstanceRepository;
    @Resource
    private InstanceProps instanceProps;
    @Resource
    private MessageHelper messageHelper;
    @Resource
    private RegistryCacheHelper registryCacheHelper;
    @Resource
    private DeferredResultHandler deferredResultHandler;

    @Transactional(rollbackFor = Exception.class)
    public void register(OpenInstanceRegisterReq req) {
        ConfInstance confInstance = OpenInstanceRegisterReq.parseInstance(req);
        confInstance.setAddTime(new Date());
        confInstance.setUpdateTime(confInstance.getAddTime());
        confInstance.setExpireTime(System.currentTimeMillis() + instanceProps.getHeartbeatInterval() * 3);
        confInstanceRepository.upsertInstance(confInstance);

        Message message = Message.builder().type(Message.CONF_INSTANCE).data(JSON.toJSONString(new InstanceMessage(req.getEnv(), req.getAppname())))
                .addTime(confInstance.getAddTime()).updateTime(confInstance.getAddTime()).build();
        messageHelper.broadcast(List.of(message));
    }

    @Transactional(rollbackFor = Exception.class)
    public void unregister(OpenInstanceRegisterReq req) {
        ConfInstance confInstance = OpenInstanceRegisterReq.parseInstance(req);
        confInstanceRepository.invalidInstance(confInstance);
        Date now = new Date();
        Message message = Message.builder().type(Message.CONF_INSTANCE).data(JSON.toJSONString(new InstanceMessage(req.getEnv(), req.getAppname())))
                .addTime(now).updateTime(now).build();
        messageHelper.broadcast(List.of(message));
    }

    public OpenInstanceDiscoveryResp discovery(OpenInstanceDiscoveryReq req) {
        Map<String,List<InstanceDTO>> instancesMap=  new HashMap<>();
        Map<String,String> instancesMD5Map=  new HashMap<>();

        for (String appname : req.getAppnames()) {
            String instancesMd5 = registryCacheHelper.getInstancesMd5(req.getEnv(), appname);
            if (instancesMd5 != null) {
                instancesMap.put(appname, registryCacheHelper.getInstances(req.getEnv(), appname));
                instancesMD5Map.put(appname, instancesMd5);
            }
        }
        return OpenInstanceDiscoveryResp.builder().instances(instancesMap).instancesMd5(instancesMD5Map).build();
    }

    public DeferredResult<OpenApiResp<PushClientEnvAppDTO>> watch(OpenInstanceDiscoveryReq req) {
        DeferredResult<OpenApiResp<PushClientEnvAppDTO>> deferredResult = new DeferredResult<>(30 * 1000L,  OpenApiResp.error("1002", "Subscribe instance timeout"));
        req.getAppnames().forEach(appname -> deferredResultHandler.addInstanceDeferredResult(req.getEnv(), appname, deferredResult));
        return deferredResult;
    }
}

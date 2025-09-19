package com.wly.config.server.dao.rep;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wly.config.server.dao.entity.ConfInstance;
import com.wly.config.server.dao.mapper.ConfInstanceMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class ConfInstanceRepository extends ServiceImpl<ConfInstanceMapper, ConfInstance> {

    public List<ConfInstance> queryEnvAndAppnames() {
        return list(Wrappers.<ConfInstance>lambdaQuery().groupBy(ConfInstance::getEnv, ConfInstance::getAppname).select(ConfInstance::getEnv, ConfInstance::getAppname));
    }

    public List<ConfInstance> queryByEnvAndAppname(String env, String appname) {
        return list(Wrappers.<ConfInstance>lambdaQuery().eq(ConfInstance::getEnv, env).eq(ConfInstance::getAppname, appname)
                .eq(ConfInstance::getStatus, ConfInstance.RUNNING).ge(ConfInstance::getExpireTime, System.currentTimeMillis()));
    }

    public boolean invalidInstance(ConfInstance instance) {
        return update(Wrappers.<ConfInstance>lambdaUpdate().set(ConfInstance::getStatus, ConfInstance.SHOUTDOWN).set(ConfInstance::getUpdateTime, new Date())
                .eq(ConfInstance::getEnv, instance.getEnv()).eq(ConfInstance::getAppname, instance.getAppname())
                .eq(ConfInstance::getIp, instance.getIp()).eq(ConfInstance::getPort, instance.getPort())
                .eq(ConfInstance::getStatus, ConfInstance.RUNNING));
    }

    public boolean upsertInstance(ConfInstance instance) {
        return getBaseMapper().upsert(instance) > 0;
    }
}

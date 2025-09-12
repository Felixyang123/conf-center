package com.wly.config.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wly.config.dao.entity.ConfData;
import com.wly.config.dao.mapper.ConfDataMapper;

import java.util.List;

public class ConfDataService extends ServiceImpl<ConfDataMapper, ConfData> {

    public List<ConfData> queryEnvAndAppname() {
        return list(Wrappers.<ConfData>lambdaQuery().select(ConfData::getEnv, ConfData::getAppname).groupBy(ConfData::getEnv, ConfData::getAppname));
    }

    public List<ConfData> queryByEnvAndAppname(String env, String appname) {
        return list(Wrappers.<ConfData>lambdaQuery().eq(ConfData::getEnv, env).eq(ConfData::getAppname, appname));
    }

    public ConfData getByEnvAndAppnameAndKey(String env, String appname, String key) {
        return getOne(Wrappers.<ConfData>lambdaQuery().eq(ConfData::getEnv, env).eq(ConfData::getAppname, appname).eq(ConfData::getKey, key));
    }
}

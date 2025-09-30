package com.wly.config.server.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wly.config.server.dao.entity.ConfAccessToken;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface ConfAccessTokenMapper extends BaseMapper<ConfAccessToken> {

    @Insert("""
             insert into conf_access_token(`appname`,`env`,`access_token`,`status`,`add_time`,`update_time`) 
             values (#{token.appname},#{token.env},#{token.accessToken},0,now(),now()) on duplicate key update `status`=0,`update_time`=now();
            """)
    int upsert(@Param("token") ConfAccessToken confAccessToken);
}

package com.wly.config.server.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wly.config.server.dao.entity.ConfInstance;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface ConfInstanceMapper extends BaseMapper<ConfInstance> {

    @Insert("""
            INSERT INTO conf_instance (
                        `env`,
                        `appname`,
                        `ip`,
                        `port`,
                        `ext`,
                        `status`,
                        `expire_time`,
                        `add_time`,
                        `update_time`
                    )
                    VALUES(
                              #{instance.env},
                              #{instance.appname},
                              #{instance.ip},
                              #{instance.port},
                              #{instance.ext},
                              #{instance.status},
                              #{instance.expireTime},
                              #{instance.addTime},
                              #{instance.updateTime}
                    )
                    ON DUPLICATE KEY UPDATE
                        `expire_time` = #{instance.expireTime}
            """)
    int upsert(@Param("instance") ConfInstance instance);
}

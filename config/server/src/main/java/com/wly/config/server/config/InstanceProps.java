package com.wly.config.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "instance")
public class InstanceProps {

    /**
     * 心跳间隔 ms
     */
    private Long heartbeatInterval;
}

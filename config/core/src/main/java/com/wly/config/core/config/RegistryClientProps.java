package com.wly.config.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Random;


@Data
@ConfigurationProperties(prefix = "instance-client")
public class RegistryClientProps {
    private String serverAddress;

    private String accessToken;

    private String env;

    private Long heartbeatInterval;

    private String host;

    private String port;

    private String ext;

    private String appname;

    public String parseServerAddress() {
        String[] addrs = serverAddress.split(",");
        if (addrs.length == 1) {
            return addrs[0];
        }
        int index = new Random().nextInt(addrs.length);
        return addrs[index];
    }
}

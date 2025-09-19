package com.wly.config.core.bean.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstanceDTO {

    private String env;

    private String appname;

    private String ip;

    private String port;

    private String ext;
}

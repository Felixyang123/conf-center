package com.wly.config.core.bean.pojo.resp;

import com.wly.config.core.bean.pojo.InstanceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenInstanceDiscoveryResp implements Serializable {
    @Serial
    private static final long serialVersionUID = 250632739868139120L;

    private String env;

    private Map<String, List<InstanceDTO>> instances;

    private Map<String, String> instancesMd5;
}

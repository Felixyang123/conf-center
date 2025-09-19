package com.wly.config.server.open.pojo.resp;

import com.wly.config.server.helper.bean.InstanceDTO;
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
    private static final long serialVersionUID = 7503967397412126435L;

    private String env;

    private Map<String, List<InstanceDTO>> instances;

    private Map<String, String> instancesMd5;
}

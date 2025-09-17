package com.wly.config.server.open.pojo.resp;

import com.wly.config.server.helper.bean.ConfDataDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenDataConfQueryResp implements Serializable {
    @Serial
    private static final long serialVersionUID = -1866506590842356442L;

    private String env;

    private Map<String, Map<String, String>> confDataMd5Map;

    private Map<String, Map<String, ConfDataDTO>> confDataMap;
}

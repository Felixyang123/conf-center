package com.wly.config.core.bean.pojo.resp;

import com.wly.config.core.bean.ConfDataDTO;
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
    private static final long serialVersionUID = -3358578812655279805L;
    private String env;

    private Map<String, Map<String, String>> confDataMd5Map;

    private Map<String, Map<String, ConfDataDTO>> confDataMap;
}

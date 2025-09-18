package com.wly.config.samples.controller;

import com.wly.config.core.annotation.ConfListen;
import com.wly.config.core.client.ConfHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample/conf")
public class ConfSampleController {
    @ConfListen(appname = "conf-samples", key = "conf-samples-key01", defaultValue = "-1")
    private String confSamplesKey01;

    @GetMapping("/get")
    public String get(String appname, String key) {
        return ConfHelper.get(appname, key, "-1");
    }

    @GetMapping("/listen")
    public String listen() {
        return confSamplesKey01;
    }
}

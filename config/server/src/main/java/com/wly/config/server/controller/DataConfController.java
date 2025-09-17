package com.wly.config.server.controller;

import com.wly.config.server.common.Result;
import com.wly.config.server.common.Results;
import com.wly.config.server.dao.entity.ConfData;
import com.wly.config.server.pojo.req.ConfDataAddReq;
import com.wly.config.server.pojo.req.ConfDataEditReq;
import com.wly.config.server.pojo.resp.ConfDataQueryResp;
import com.wly.config.server.service.ConfDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataConfController {
    private final ConfDataService confDataService;

    /**
     * 添加配置
     *
     * @param req
     * @return
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody ConfDataAddReq req) {
        confDataService.save(ConfDataAddReq.transfer(req));
        return Results.ok();
    }

    /**
     * 变更配置
     *
     * @param req
     * @return
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody ConfDataEditReq req) {
        confDataService.updateById(ConfDataEditReq.transfer(req));
        return Results.ok();
    }

    /**
     * 分页查询配置
     * @param env
     * @param appname
     * @return
     */
    @GetMapping("/page")
    public Result<List<ConfDataQueryResp>> page(@RequestParam String env, @RequestParam String appname) {
        List<ConfData> confDataList = confDataService.queryByEnvAndAppname(env, appname);
        return Results.ok(confDataList.stream().map(ConfDataQueryResp::buildFromConfData).toList());
    }
}

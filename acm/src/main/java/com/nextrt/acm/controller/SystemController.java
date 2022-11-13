package com.nextrt.acm.controller;

import com.nextrt.core.vo.Result;
import com.nextrt.acm.service.SystemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/system", produces = "application/json;charset=UTF-8")
public class SystemController {
    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }
    @GetMapping("/info")
    public Result getInfo(@RequestParam String param) {
        return systemService.getCommonSystemInfo(param);
    }
}

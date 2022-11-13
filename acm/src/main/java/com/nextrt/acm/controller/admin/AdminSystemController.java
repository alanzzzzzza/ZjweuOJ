package com.nextrt.acm.controller.admin;

import com.nextrt.acm.util.file.LocalFileUtil;
import com.nextrt.core.vo.Result;
import com.nextrt.core.entity.common.SysConfig;
import com.nextrt.acm.service.SystemService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/admin/config", produces = "application/json;charset=UTF-8")
public class AdminSystemController {
    private final SystemService systemService;

    public AdminSystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @GetMapping("get")
    public Result getConfigs(){
        return systemService.getSystemConfig();
    }

    @PostMapping("update")
    public Result updateConfig(@RequestBody SysConfig sysConfig){
        return systemService.updateSystemConfig(sysConfig);
    }

    @PostMapping("backup")
    public Result BackUpData(HttpServletResponse response){
        Result result =  systemService.backupSQLData();
        if (result.getData() != null) {
            LocalFileUtil.downloadFile(response, result.getData().toString());
            return null;
        }
        return result;
    }
}

package com.nextrt.acm.service;

import com.nextrt.acm.service.system.SQLBackupService;
import com.nextrt.core.vo.Result;
import com.nextrt.acm.biz.system.ConfigBiz;
import com.nextrt.core.entity.common.SysConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class SystemService {

    private final ConfigBiz config;
    @Autowired
    private SQLBackupService sqlBackupService;

    public SystemService(ConfigBiz config) {
        this.config = config;
    }

    public Result getSystemConfig(){
        return new Result(1,"系统配置获取成功",config.getSystemConfig());
    }
    public Result getCommonSystemInfo( String param) {
        String[] allow = {"UserAgreement", "SiteName", "fileWhiteSuffix"};
        if (Arrays.asList(allow).contains(param)) {
            return new Result(1, "Success", config.getString(param));
        }
        return new Result(-1, "权限错误");
    }
    public Result updateSystemConfig(SysConfig sysConfig){
        if(config.update(sysConfig) > 0){
            return new Result(1,"设置更新成功");
        }
        return new Result(-1,"设置更新失败");
    }

    public Result backupSQLData(){
        return new Result(1,"数据库备份成功", sqlBackupService.exportDataBase());
    }

}

package com.nextrt.acm.biz.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nextrt.core.entity.common.SysConfig;
import com.nextrt.core.mapper.system.SysConfigMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@CacheConfig(cacheNames = "SystemConfig",cacheManager = "cacheManagerTwoHour")
public class ConfigBiz {
    private final SysConfigMapper sysConfigMapper;

    public ConfigBiz(SysConfigMapper sysConfigMapper) {
        this.sysConfigMapper = sysConfigMapper;
    }
    @Cacheable(value = "SystemConfig",key = "'String-'+#name",unless = "#result == null")
    public String getString(String name) {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysConfig::getName, name);
        SysConfig sysConfig = sysConfigMapper.selectOne(queryWrapper);
        if (sysConfig != null)
            return sysConfig.getValue();
        return null;
    }
    @Cacheable(value = "SystemConfig",key = "'int-'+#name",unless = "#result == null")
    public Integer getInt(String name) {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysConfig::getName, name);
        SysConfig sysConfig = sysConfigMapper.selectOne(queryWrapper);
        if (sysConfig != null)
            return Integer.parseInt(sysConfig.getValue());
        return 0;
    }
    @Cacheable(value = "SystemConfig",key = "'allSetting'",unless = "#result == null")
    public List<SysConfig> getSystemConfig(){
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda(). gt(SysConfig::getType,0);
        return sysConfigMapper.selectList(queryWrapper);
    }
    @CacheEvict(value = "SystemConfig",allEntries = true)
    public int update(SysConfig sysConfig) {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysConfig::getName, sysConfig.getName());
        return sysConfigMapper.update(sysConfig, queryWrapper);
    }
}

package com.nextrt.acm.biz.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.common.Help;
import com.nextrt.core.mapper.system.HelpsMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;


@Component
@CacheConfig(cacheNames = "Helps",cacheManager = "cacheManagerTwoHour")
public class HelpsBiz {
    private final HelpsMapper helpsMapper;

    public HelpsBiz(HelpsMapper helpsMapper) {
        this.helpsMapper = helpsMapper;
    }

    @CacheEvict(value = "Helps",allEntries = true)
    public synchronized int addHelp(Help help) {
        return helpsMapper.insert(help);
    }

    @Cacheable(value = "Helps",key = "'id-'+#id",unless = "#result == null")
    public Help getHelp(int id) {
        return helpsMapper.selectById(id);
    }

    @CacheEvict(value = "Helps",allEntries = true)
    public int updateHelp(Help help) {
        return helpsMapper.updateById(help);
    }

    @CacheEvict(value = "Helps",allEntries = true)
    public int deleteHelpById(int id) {
        return helpsMapper.deleteById(id);
    }

    @Cacheable(value = "Helps",key = "'IPage-'+#page+'-'+#size",unless = "#result == null")
    public IPage<Help> getHelps(int page, int size) {
        Page<Help> aPage = new Page<Help>(page, size);
        QueryWrapper<Help> queryWrapper = new QueryWrapper<Help>();
        queryWrapper.lambda().select(Help::getId,Help::getTitle,Help::getAddTime).orderByDesc(Help::getId);
        return helpsMapper.selectPage(aPage,queryWrapper);
    }

}

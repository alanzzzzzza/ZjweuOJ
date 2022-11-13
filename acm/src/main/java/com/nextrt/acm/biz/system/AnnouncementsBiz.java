package com.nextrt.acm.biz.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.common.Announcement;
import com.nextrt.core.mapper.system.AnnouncementsMapper;
import com.nextrt.core.vo.system.AnnouncementVO;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@CacheConfig(cacheNames = "Announcements",cacheManager = "cacheManager")
public class AnnouncementsBiz {
    private final AnnouncementsMapper announcementsMapper;

    public AnnouncementsBiz(AnnouncementsMapper announcementsMapper) {
        this.announcementsMapper = announcementsMapper;
    }


    @CacheEvict(value = "Announcements",allEntries = true)
    public synchronized int addAnnouncement(Announcement announcement) {
        return announcementsMapper.insert(announcement);
    }

    @CacheEvict(value = "Announcements",allEntries = true)
    public int deleteAnnouncementById(int id) {
        return announcementsMapper.deleteById(id);
    }

    @CacheEvict(value = "Announcements",allEntries = true)
    public int updateAnnouncement(Announcement announcement) {
        return announcementsMapper.updateById(announcement);
    }

    @Cacheable(value = "Announcements",key = "'id-'+#id",unless = "#result == null")
    public Announcement getAnnouncement(int id) {
        return announcementsMapper.selectById(id);
    }


    @Cacheable(value = "Announcements",key = "'user-'+#page+'-'+#size+'-'+#showType",unless = "#result == null")
    public IPage<AnnouncementVO> userGetAnnouncements(int page, int size, int showType) {
        Page<AnnouncementVO> aPage = new Page<AnnouncementVO>(page, size);
        return announcementsMapper.getAnnouncementVOByUser(aPage,showType,new Date());
    }

    @Cacheable(value = "Announcements",key = "'admin-'+#page+'-'+#size+'-'+#showType",unless = "#result == null")
    public IPage<AnnouncementVO> adminGetAnnouncements(int page, int size, int showType) {
        Page<AnnouncementVO> aPage = new Page<AnnouncementVO>(page, size);
        return announcementsMapper.getAnnouncementVOByAdmin(aPage,showType);
    }

}

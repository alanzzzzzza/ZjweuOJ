package com.nextrt.acm.service;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.core.vo.system.AnnouncementVO;
import com.nextrt.acm.biz.system.AnnouncementsBiz;
import com.nextrt.core.entity.common.Announcement;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AnnouncementService {
    private final AnnouncementsBiz announcementsBiz;

    public AnnouncementService(AnnouncementsBiz announcementsBiz) {
        this.announcementsBiz = announcementsBiz;
    }

    public int addAnnouncement(Announcement announcement) {
        if (announcement.getExpireTime().getTime() <= new Date().getTime())
            announcement.setExpireTime(DateUtil.parse("2050-01-01"));
        return announcementsBiz.addAnnouncement(announcement);
    }

    public int deleteAnnouncementById(int id) {
        return announcementsBiz.deleteAnnouncementById(id);
    }

    public int updateAnnouncement(Announcement announcement) {
        return announcementsBiz.updateAnnouncement(announcement);
    }

    public Announcement getAnnouncement(int id) {
        return announcementsBiz.getAnnouncement(id);
    }

    public IPage<AnnouncementVO> userGetAnnouncement(int page, int size, int showType) {
        return announcementsBiz.userGetAnnouncements(page, size, showType);
    }

    public IPage<AnnouncementVO> adminGetAnnouncement(int page, int size, int showType) {
        return announcementsBiz.adminGetAnnouncements(page, size, showType);
    }
}

package com.nextrt.core.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.common.Announcement;
import com.nextrt.core.vo.system.AnnouncementVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AnnouncementsMapper extends BaseMapper<Announcement> {

    @Select("select u.nick,ann.id,ann.title,ann.add_time from announcements as ann,user as u where u.user_id = ann.user_id and ann.show_type<=#{showType} and  ann.show_time<#{nowDate} and ann.expire_time>#{nowDate} order by id desc")
    IPage<AnnouncementVO> getAnnouncementVOByUser(Page<?> page, int showType, Date nowDate);

    @Select("select u.nick,ann.id,ann.title,ann.add_time,ann.expire_time,ann.show_time from announcements as ann,user as u where u.user_id = ann.user_id and ann.show_type<=#{showType} order by id desc")
    IPage<AnnouncementVO> getAnnouncementVOByAdmin(Page<?> page, int showType);
}
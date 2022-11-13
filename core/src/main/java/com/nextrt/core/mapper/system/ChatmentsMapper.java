package com.nextrt.core.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.common.Chatment;
import com.nextrt.core.vo.system.ChatmentVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatmentsMapper extends BaseMapper<Chatment> {

    @Select("select u.username,ch.id,ch.title,ch.add_time from chatments as ch,user as u where u.user_id = ch.user_id order by id desc")
    IPage<ChatmentVO> getChatmentVOByUser(Page<?> page);

    @Select("select u.user_id, u.username,ch.id,ch.title,ch.add_time,ch.content from chatments as ch,user as u where u.user_id = ch.user_id and ch.id = #{id} order by id desc")
    ChatmentVO getChatmentVOById(int id);
}
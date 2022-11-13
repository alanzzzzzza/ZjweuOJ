package com.nextrt.core.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.common.ChatmentInfo;
import com.nextrt.core.vo.system.ChatmentInfoVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatmentInfoMapper extends BaseMapper<ChatmentInfo> {
    @Select("select u.username,ch.id,ch.content,ch.add_time,ch.pid,ch.user_id from chatment_info as ch,user as u where u.user_id = ch.user_id and ch.pid = #{pid} order by ch.id asc")
    IPage<ChatmentInfoVO> getChatmentInfoByUser(Page<?> page, int pid);

    @Select("select u.username,ch.id,ch.content,ch.add_time,ch.pid,ch.user_id from chatment_info as ch,user as u where u.user_id = ch.user_id and ch.id = #{cid}")
    ChatmentInfoVO getChatmentInfoById(int cid);
}
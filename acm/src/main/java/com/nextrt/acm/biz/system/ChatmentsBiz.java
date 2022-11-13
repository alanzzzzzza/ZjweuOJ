package com.nextrt.acm.biz.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.common.Chatment;
import com.nextrt.core.mapper.system.ChatmentsMapper;
import com.nextrt.core.vo.system.AnnouncementVO;
import com.nextrt.core.vo.system.ChatmentVO;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = "Chatments",cacheManager = "cacheManager")
public class ChatmentsBiz {
    private final ChatmentsMapper chatmentsMapper;

    public ChatmentsBiz(ChatmentsMapper chatmentsMapper) {
        this.chatmentsMapper = chatmentsMapper;
    }

    @CacheEvict(value = "Chatments",allEntries = true)
    public synchronized int addChatment(Chatment chatment) {
        return chatmentsMapper.insert(chatment);
    }

    @CacheEvict(value = "Chatments",allEntries = true)
    public int deleteChatmentById(int id) {
        return chatmentsMapper.deleteById(id);
    }

    @CacheEvict(value = "Chatments",allEntries = true)
    public int updateChatment(Chatment chatment) {
        return chatmentsMapper.updateById(chatment);
    }

    @Cacheable(value = "Chatments",key = "'id-'+#id",unless = "#result == null")
    public ChatmentVO getChatment(int id) {
        return chatmentsMapper.getChatmentVOById(id);
    }

    @Cacheable(value = "Chatments",key = "'user-'+#page+'-'+#size",unless = "#result == null")
    public IPage<ChatmentVO> userGetChatments(int page, int size) {
        Page<AnnouncementVO> aPage = new Page<AnnouncementVO>(page, size);
        return chatmentsMapper.getChatmentVOByUser(aPage);
    }
}

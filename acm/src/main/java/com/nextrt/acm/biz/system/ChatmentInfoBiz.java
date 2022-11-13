package com.nextrt.acm.biz.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.common.ChatmentInfo;
import com.nextrt.core.mapper.system.ChatmentInfoMapper;
import com.nextrt.core.vo.system.ChatmentInfoVO;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = "ChatmentInfo",cacheManager = "cacheManager")
public class ChatmentInfoBiz {
    private final ChatmentInfoMapper chatmentInfoMapper;

    public ChatmentInfoBiz(ChatmentInfoMapper chatmentInfoMapper) {
        this.chatmentInfoMapper = chatmentInfoMapper;
    }

    @CacheEvict(value = "ChatmentInfo",allEntries = true)
    public synchronized int addChatmentInfo(ChatmentInfo chatmentInfo) {
        return chatmentInfoMapper.insert(chatmentInfo);
    }

    @CacheEvict(value = "ChatmentInfo",allEntries = true)
    public int deleteChatmentInfoById(int id) {
        return chatmentInfoMapper.deleteById(id);
    }

    @Cacheable(value = "ChatmentInfo",key = "'id-'+#id",unless = "#result == null")
    public ChatmentInfoVO getChatmentInfoById(int id) {
        return chatmentInfoMapper.getChatmentInfoById(id);
    }

    @Cacheable(value = "ChatmentInfo",key = "'user-'+#page+'-'+#size+'-'+#pid",unless = "#result == null")
    public IPage<ChatmentInfoVO> userGetChatments(int page, int size, int pid) {
        Page<ChatmentInfo> aPage = new Page<ChatmentInfo>(page, size);
        return chatmentInfoMapper.getChatmentInfoByUser(aPage, pid);
    }
}
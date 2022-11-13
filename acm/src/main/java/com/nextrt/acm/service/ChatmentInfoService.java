package com.nextrt.acm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.acm.biz.system.ChatmentInfoBiz;
import com.nextrt.core.entity.common.ChatmentInfo;
import com.nextrt.core.vo.system.ChatmentInfoVO;
import org.springframework.stereotype.Service;

@Service
public class ChatmentInfoService {
    private final ChatmentInfoBiz chatmentInfoBiz;

    public ChatmentInfoService(ChatmentInfoBiz chatmentInfoBiz) {
        this.chatmentInfoBiz = chatmentInfoBiz;
    }

    public int addChatmentInfo(ChatmentInfo chatmentInfo) {
        return chatmentInfoBiz.addChatmentInfo(chatmentInfo);
    }

    public int deleteChatmentInfoById(int id) {
        return chatmentInfoBiz.deleteChatmentInfoById(id);
    }

    public ChatmentInfoVO getChatmentInfoById(int id) {
        return chatmentInfoBiz.getChatmentInfoById(id);
    }

    public IPage<ChatmentInfoVO> userGetChatments(int page, int size, int pid) {
        return chatmentInfoBiz.userGetChatments(page, size, pid);
    }
}

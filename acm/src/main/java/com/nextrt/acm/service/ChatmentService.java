package com.nextrt.acm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.acm.biz.system.ChatmentsBiz;
import com.nextrt.core.entity.common.Chatment;
import com.nextrt.core.vo.system.ChatmentVO;
import org.springframework.stereotype.Service;

@Service
public class ChatmentService {
    private final ChatmentsBiz chatmentsBiz;

    public ChatmentService(ChatmentsBiz chatmentsBiz) {
        this.chatmentsBiz = chatmentsBiz;
    }

    public int addChatment(Chatment chatment) {
        return chatmentsBiz.addChatment(chatment);
    }

    public int deleteChatmentById(int id) {
        return chatmentsBiz.deleteChatmentById(id);
    }

    public int updateChatment(Chatment chatment) {
        return chatmentsBiz.updateChatment(chatment);
    }

    public ChatmentVO getChatment(int id) {
        return chatmentsBiz.getChatment(id);
    }

    public IPage<ChatmentVO> userGetChatments(int page, int size) {
        return chatmentsBiz.userGetChatments(page, size);
    }
}

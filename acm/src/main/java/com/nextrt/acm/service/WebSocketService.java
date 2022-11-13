package com.nextrt.acm.service;

import com.nextrt.acm.config.thread.ViThreadPoolManager;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String url, String msg) {
        ViThreadPoolManager.getInstance().execute(() -> {
            //一条立即发送
            messagingTemplate.convertAndSend(url, msg);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //一条等待3秒后发送，等待用户打开页面
            messagingTemplate.convertAndSend(url, msg);
        });
    }
}

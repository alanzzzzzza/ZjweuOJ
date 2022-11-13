package com.nextrt.judge.service.mq;

import com.nextrt.judge.vo.JudgeResult;
import com.nextrt.judge.vo.SystemInfo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import static com.nextrt.judge.config.SystemConfig.CJudgeHeart;
import static com.nextrt.judge.config.SystemConfig.CJudgeResult;

@Service
public class RabbitMqSend {
    private final AmqpTemplate amqpTemplate;

    public RabbitMqSend(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendHeart(SystemInfo systemInfo) {
        amqpTemplate.convertAndSend(CJudgeHeart, systemInfo );
    }
    public void sendResult(JudgeResult result) {
        amqpTemplate.convertAndSend(CJudgeResult, result );
    }
}

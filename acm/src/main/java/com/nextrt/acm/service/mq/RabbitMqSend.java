package com.nextrt.acm.service.mq;

import com.nextrt.core.vo.judge.JudgeTask;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import static com.nextrt.acm.config.SystemConfig.CJudgeTask;

@Service
public class RabbitMqSend {
    private final AmqpTemplate amqpTemplate;

    public RabbitMqSend(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendJudgeTask(JudgeTask judgeTask) {
        amqpTemplate.convertAndSend(CJudgeTask, judgeTask );
    }
}

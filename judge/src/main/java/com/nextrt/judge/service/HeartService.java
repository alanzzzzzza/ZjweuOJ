package com.nextrt.judge.service;

import com.nextrt.judge.service.mq.RabbitMqSend;
import com.nextrt.judge.vo.SystemInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.nextrt.judge.config.SystemConfig.DealNum;
import static com.nextrt.judge.config.SystemConfig.Version;
import static com.nextrt.judge.util.SystemUtil.getSystemInfo;


@Service
public class HeartService {
    private final RabbitMqSend rabbitMqSend;

    public HeartService(RabbitMqSend rabbitMqSend) {
        this.rabbitMqSend = rabbitMqSend;
    }

    @Scheduled(cron = "1 * * * * *")
    public void sendHeart(){
        SystemInfo systemInfo = getSystemInfo();
        systemInfo.setJudgeVersion(Version);
        systemInfo.setDealNum(DealNum);
        rabbitMqSend.sendHeart(systemInfo);
    }
}

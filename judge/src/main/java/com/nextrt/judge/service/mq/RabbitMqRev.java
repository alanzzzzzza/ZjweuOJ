package com.nextrt.judge.service.mq;
import com.nextrt.judge.config.thread.ViThreadPoolManager;
import com.nextrt.judge.service.JudgeService;
import com.nextrt.judge.vo.JudgeTask;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.nextrt.judge.config.SystemConfig.CJudgeTask;

@Service
public class RabbitMqRev {

    private final JudgeService judgeService;

    public RabbitMqRev(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @RabbitListener(queues = CJudgeTask)
    @Async
    public void dealJudgeTask(JudgeTask judgeTask){
        ViThreadPoolManager.getInstance().execute(() -> judgeService.judge(judgeTask));
    }

}

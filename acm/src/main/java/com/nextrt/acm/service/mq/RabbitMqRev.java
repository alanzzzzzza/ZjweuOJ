package com.nextrt.acm.service.mq;

import com.nextrt.acm.service.JudgeService;
import com.nextrt.acm.service.SubmissionService;
import com.nextrt.core.vo.judge.JudgeResult;
import com.nextrt.acm.config.thread.ViThreadPoolManager;
import com.nextrt.core.entity.judge.JudgeServer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.nextrt.acm.config.SystemConfig.CJudgeHeart;
import static com.nextrt.acm.config.SystemConfig.CJudgeResult;

@Service
public class RabbitMqRev {
    private final JudgeService judgeService;
    private final SubmissionService submissionService;

    public RabbitMqRev(JudgeService judgeService, SubmissionService submissionService) {
        this.judgeService = judgeService;
        this.submissionService = submissionService;
    }

    //判题服务器心跳信息处理
    @RabbitListener(queues = CJudgeHeart)
    public void dealHeart(JudgeServer judgeServer){
        ViThreadPoolManager.getInstance().execute(() -> judgeService.updateHeart(judgeServer));
    }

    //接收判题信息回调
    @RabbitListener(queues = CJudgeResult)
    public void dealResult(JudgeResult result){
        ViThreadPoolManager.getInstance().execute(() -> submissionService.dealResult(result));
    }
}

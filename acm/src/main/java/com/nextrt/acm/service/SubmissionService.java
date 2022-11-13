package com.nextrt.acm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.acm.biz.exercise.ProblemBiz;
import com.nextrt.acm.biz.exercise.SubmissionBiz;
import com.nextrt.acm.biz.judge.JudgeServerBiz;
import com.nextrt.acm.config.thread.ViThreadPoolManager;
import com.nextrt.acm.service.mq.RabbitMqSend;
import com.nextrt.acm.service.system.UpdateService;
import com.nextrt.core.entity.exercise.Problem;
import com.nextrt.core.entity.exercise.ProblemTestData;
import com.nextrt.core.entity.exercise.Submission;
import com.nextrt.core.vo.contest.SubmissionVO;
import com.nextrt.core.vo.judge.JudgeResult;
import com.nextrt.core.vo.judge.JudgeTask;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SubmissionService {
    private final SubmissionBiz submissionBiz;
    private final ProblemBiz problemBiz;
    private final RabbitMqSend rabbitMqSend;
    private final JudgeServerBiz judgeServerBiz;
    private final UpdateService updateService;
    private final WebSocketService webSocketService;

    public SubmissionService(SubmissionBiz submissionBiz, ProblemBiz problemBiz, RabbitMqSend rabbitMqSend, JudgeServerBiz judgeServerBiz, UpdateService updateService, WebSocketService webSocketService) {
        this.submissionBiz = submissionBiz;
        this.problemBiz = problemBiz;
        this.rabbitMqSend = rabbitMqSend;
        this.judgeServerBiz = judgeServerBiz;
        this.updateService = updateService;
        this.webSocketService = webSocketService;
    }

    public int addSubmission(Submission submission) {
        Problem problem = problemBiz.getProblemById(submission.getProblemId());
        if (problem == null) return -1;
        submission.setStatus(-1);
        submission.setSubTime(new Date());
        int status = submissionBiz.addSubmission(submission);
        if (status == 1) {
            if (submission.getContestId() == 0) {//练习模块 增加问题提交次数
                problem.setSubmissionNumber(problem.getSubmissionNumber() + 1);
                problemBiz.updateProblem(problem);
            } else {
                updateService.updateContestResult(submission, problem);
            }
            dealSubmission();
        }
        return status;
    }

    public int updateSubmission(Submission submission) {
        Problem problem = problemBiz.getProblemById(submission.getProblemId());
        if (problem == null) return -1;
        submission.setStatus(-1);
        int status = submissionBiz.updateSubmission(submission);
        if (status == 1) {
            if (submission.getContestId() == 0) {//练习模块 增加问题提交次数
                problem.setSubmissionNumber(problem.getSubmissionNumber() + 1);
                problemBiz.updateProblem(problem);
            } else {
                updateService.updateContestResult(submission, problem);
            }
            dealSubmission();
        }
        return status;
    }

    //游客查看提交信息
    public IPage<SubmissionVO> TouristsGetSubmission(int problemId, int userId, int page, int size) {
        return submissionBiz.TouristsGetSubmission(problemId, userId, page, size);
    }

    //游客查看提交信息
    public IPage<SubmissionVO> AdminGetSubmission(int problemId, int userId, int page, int size) {
        return submissionBiz.AdminGetSubmission(problemId, userId, page, size);
    }

    public void dealResult(JudgeResult result) {
        Submission submission = submissionBiz.getSubmissionById(result.getSubId());
        if (submission.getStatus() == -1) {//如果问题为未判题状态
            submission.setStatus(result.getStatus());
            submission.setJudgeTime(new Date());
            submission.setMemoryUse(result.getMemoryUsed());
            submission.setTimeUse(result.getTimeUsed());
            submission.setGotScore(result.getGotScore());
            submission.setScore(result.getScore());
            submission.setJudgeUseTime(result.getJudgeTime());
            submission.setOtherInfo(result.getErrorMessage());
            submission.setJudgeId(judgeServerBiz.selectJudgeServerBySN(result.getSn()).getId());
            submissionBiz.updateSubmission(submission);//更新判题结果
            if (submission.getContestId() > 0) {
                updateService.updateContestStatistical(submission.getContestId(), submission.getProblemId());//更新竞赛该题目统计数据
                updateService.updateContestResult(submission, result);//更新用户竞赛情况
                updateService.updateContestUserInfo(submission.getUserId(), submission.getContestId());
                webSocketService.sendMessage("/topic/contest/" + submission.getContestId(), "ok");
                webSocketService.sendMessage("/topic/contest/user/" + submission.getContestId(), "ok");
            } else {
                updateService.updateUserStatistical(submission.getUserId());//更新用户答题数据情况
                if (result.getStatus() == 0)
                    updateService.updateProblemAcceptedNumber(submission.getProblemId());//更新问题解决数
                webSocketService.sendMessage("/topic/user/" + submission.getUserId(), "ok");
            }
        }
    }

    //处理系统中没有判题的数据
    @Scheduled(cron = "1 */5 * * * *")
    public void dealSubmission() {
        List<Submission> submissionList = submissionBiz.getUnJudgeList();
        submissionList.forEach(x -> ViThreadPoolManager.getInstance().execute(() -> {
            Problem problem = problemBiz.getProblemById(x.getProblemId());
            JudgeTask judgeTask = JudgeTask.builder()
                    .timeLimit(problem.getTimeLimit()).memoryLimit(problem.getMemoryLimit())
                    .type(x.getType()).subId(x.getId()).judgeId(caseLanguage(x.getLanguage())).src(x.getCode())
                    .build();
            List<String> in = new ArrayList<>();
            List<String> out = new ArrayList<>();
            List<ProblemTestData> problemTestData = problemBiz.getProblemTestDataByProblemId(x.getProblemId());
            if (x.getType() == 0) {
                problemTestData.forEach(test -> {
                    in.add(test.getInput());
                    out.add(test.getOutput());
                });
            } else {
                List<Integer> score = new ArrayList<>();
                problemTestData.forEach(test -> {
                    in.add(test.getInput());
                    out.add(test.getOutput());
                    score.add(test.getScore());
                });
                judgeTask.setScore(score);
            }
            judgeTask.setInput(in);
            judgeTask.setOutput(out);
            rabbitMqSend.sendJudgeTask(judgeTask);
        }));
    }

    public int caseLanguage(String language) {
        switch (language) {
            case "C++":
                return 2;
            case "Java":
                return 3;
            case "Python2":
                return 4;
            case "Python3":
                return 5;
            case "Ruby":
                return 6;
            case "Go":
                return 7;
            case "C#":
                return 8;
            default:
                return 1;
        }
    }
}

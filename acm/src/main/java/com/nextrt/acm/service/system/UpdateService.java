package com.nextrt.acm.service.system;

import com.nextrt.acm.biz.contest.ContestBiz;
import com.nextrt.acm.biz.contest.ContestJoinInfoBiz;
import com.nextrt.acm.biz.contest.ContestProblemBiz;
import com.nextrt.acm.biz.contest.ContestProblemResultBiz;
import com.nextrt.acm.biz.exam.ExamUserBiz;
import com.nextrt.acm.biz.exercise.ProblemBiz;
import com.nextrt.acm.biz.exercise.SubmissionBiz;
import com.nextrt.acm.biz.user.UserBiz;
import com.nextrt.acm.config.thread.ViThreadPoolManager;
import com.nextrt.core.entity.contest.Contest;
import com.nextrt.core.entity.contest.ContestJoinInfo;
import com.nextrt.core.entity.contest.ContestProblem;
import com.nextrt.core.entity.contest.ContestProblemResult;
import com.nextrt.core.entity.exam.ExamUser;
import com.nextrt.core.entity.exercise.Problem;
import com.nextrt.core.entity.exercise.Submission;
import com.nextrt.core.entity.user.User;
import com.nextrt.core.vo.judge.JudgeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UpdateService {
    private final ContestProblemBiz contestProblemBiz;
    private final SubmissionBiz submissionBiz;
    private final UserBiz userBiz;
    private final ContestProblemResultBiz contestProblemResultBiz;
    private final ProblemBiz problemBiz;
    private final ContestJoinInfoBiz contestJoinInfoBiz;
    private final ExamUserBiz examUserBiz;
    private final ContestBiz contestBiz;

    public UpdateService(ContestProblemBiz contestProblemBiz, SubmissionBiz submissionBiz, UserBiz userBiz, ContestProblemResultBiz contestProblemResultBiz, ProblemBiz problemBiz, ContestJoinInfoBiz contestJoinInfoBiz, ExamUserBiz examUserBiz, ContestBiz contestBiz) {
        this.contestProblemBiz = contestProblemBiz;
        this.submissionBiz = submissionBiz;
        this.userBiz = userBiz;
        this.contestProblemResultBiz = contestProblemResultBiz;
        this.problemBiz = problemBiz;
        this.contestJoinInfoBiz = contestJoinInfoBiz;
        this.examUserBiz = examUserBiz;
        this.contestBiz = contestBiz;
    }

    //定时更新非竞赛模式下用户提交解决数据
    @Async
    @Scheduled(cron = "1 0 * * * *")
    public void updateAllUserStatistical() {
        List<User> userList = userBiz.getAllUser();
        userList.forEach(x -> ViThreadPoolManager.getInstance().execute(() -> {//1用户提交数 2用户通过数 3 用户通过问题数 4用户参与题目数
            int acNum = submissionBiz.ordinaryCountNum(x.getUserId(), 2);
            int subNum = submissionBiz.ordinaryCountNum(x.getUserId(), 1);
            int acpNum = submissionBiz.ordinaryCountNum(x.getUserId(), 4);
            int subpNum = submissionBiz.ordinaryCountNum(x.getUserId(), 3);
            if (x.getAcNum() != acNum || x.getSubNum() != subNum || x.getSubpNum() != subpNum || x.getAcpNum() != acpNum) {
                x.setAcNum(acNum);
                x.setAcpNum(acpNum);
                x.setSubNum(subNum);
                x.setSubpNum(subpNum);
                userBiz.updateUserById(x);
            }
        }));
    }

    //更新非竞赛模式下用户提交解决数据
    public void updateUserStatistical(int userId) {
        User user = userBiz.getUserById(userId);
        ViThreadPoolManager.getInstance().execute(() -> {//1用户提交数 2用户通过数 3 用户通过问题数 4用户参与题目数
            int subNum = submissionBiz.ordinaryCountNum(user.getUserId(), 1);
            int acNum = submissionBiz.ordinaryCountNum(user.getUserId(), 2);
            int subpNum = submissionBiz.ordinaryCountNum(user.getUserId(), 3);
            int acpNum = submissionBiz.ordinaryCountNum(user.getUserId(), 4);
            if (user.getAcNum() != acNum || user.getSubNum() != subNum || user.getSubpNum() != subpNum || user.getAcpNum() != acpNum) {
                user.setAcNum(acNum);
                user.setAcpNum(acpNum);
                user.setSubNum(subNum);
                user.setSubpNum(subpNum);
                userBiz.updateUserById(user);
            }
        });
    }

    public void updateContestResult(Submission submission, Problem problem) {
        ContestProblemResult result = contestProblemResultBiz.getContestProblemResult(submission.getUserId(), submission.getContestId(), submission.getProblemId());
        if (result == null)//没有提交记录，初始化提交记录
        {
            result = new ContestProblemResult();
            result.setProblemName(problem.getTitle());
            result.setContestId(submission.getContestId());
            result.setProblemId(submission.getProblemId());
            result.setUserId(submission.getUserId());
            result.setStatus(-1);
            result.setSubNum(1);
            result.setScore(0);
            contestProblemResultBiz.add(result);
        } else {
            if (result.getAcTime() == null) {//没有AC增加提交次数
                result.setSubNum(result.getSubNum() + 1);
                contestProblemResultBiz.update(result);
            }
        }
    }

    public void updateContestResult(Submission submission, JudgeResult judgeResult) {
        ContestProblemResult result = contestProblemResultBiz.getContestProblemResult(submission.getUserId(), submission.getContestId(), submission.getProblemId());
        if (result.getStatus() != 0) {
            if (submission.getType() == 1) {//OI模式
                if (result.getScore() <= judgeResult.getGotScore()) {//当分数更高的时候才更新数据
                    result.setScore(judgeResult.getScore());
                    result.setStatus(judgeResult.getStatus());
                    if (judgeResult.getStatus() == 0)
                        result.setAcTime(new Date());
                    contestProblemResultBiz.update(result);
                }
            } else {
                result.setStatus(judgeResult.getStatus());
                result.setScore(judgeResult.getScore());
                if (judgeResult.getStatus() == 0)
                    result.setAcTime(new Date());
                contestProblemResultBiz.update(result);
            }
        }
    }

    //更新竞赛解决数据统计
    @Async
    public void updateContestStatistical(int contestId) {
        List<ContestProblem> list = contestProblemBiz.getContestProblemId(contestId);
        list.forEach(this::updateContestStatistical);
    }

    @Async
    public void updateContestStatistical(ContestProblem contestProblem) {
        int allSubNum = submissionBiz.countContestProblemNum(contestProblem.getContestId(), contestProblem.getContestId(), 1);
        int acNum = submissionBiz.countContestProblemNum(contestProblem.getContestId(), contestProblem.getContestId(), 2);
        contestProblem.setAcceptNum(acNum);
        contestProblem.setSubmissionNum(allSubNum);
        contestProblemBiz.updateContestProblem(contestProblem);
    }

    @Async
    public void updateContestStatistical(int contestId, int problemId) {
        ContestProblem contestProblem = contestProblemBiz.getContestProblem(problemId, contestId);
        if (contestProblem == null)
            return;
        int allSubNum = submissionBiz.countContestProblemNum(contestId, problemId, 1);
        int acNum = submissionBiz.countContestProblemNum(contestId, problemId, 2);
        contestProblem.setAcceptNum(acNum);
        contestProblem.setSubmissionNum(allSubNum);
        contestProblemBiz.updateContestProblem(contestProblem);
    }

    //更新题目答题数据
    //更新问题解决数
    public void updateProblemAcceptedNumber(int problemId) {
        Problem problem = problemBiz.getProblemById(problemId);
        problem.setAcceptedNumber(problem.getAcceptedNumber() + 1);
        problemBiz.updateProblem(problem);
    }

    //0 普通用户 1测试用户
    public void updateContestUserInfo(int userId, int contestId) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest.getIsTestAccount() == 0) {
            ContestJoinInfo contestJoinInfo = contestJoinInfoBiz.getContestJoinInfo(contestId, userId);
            if (contestJoinInfo != null) {
                int sub = submissionBiz.countContestUserDataNum(contestId, userId, 1);
                int ac = submissionBiz.countContestUserDataNum(contestId, userId, 2);
                List<ContestProblemResult> list = contestProblemResultBiz.getUserContestProblemResult(userId, contestId);
                int score = list.stream().map(ContestProblemResult::getScore).mapToInt(x -> x).sum();
                contestJoinInfo.setAc(ac);
                contestJoinInfo.setSub(sub);
                contestJoinInfo.setScore(score);
                contestJoinInfoBiz.updateContestJoinInfo(contestJoinInfo);
            }
        } else {
            ExamUser examUser = examUserBiz.getExamUserById(userId);
            if (examUser != null) {
                int sub = submissionBiz.countContestUserDataNum(contestId, userId, 1);
                int ac = submissionBiz.countContestUserDataNum(contestId, userId, 2);
                List<ContestProblemResult> list = contestProblemResultBiz.getUserContestProblemResult(userId, contestId);
                int score = list.stream().map(ContestProblemResult::getScore).mapToInt(x -> x).sum();
                examUser.setAc(ac);
                examUser.setSub(sub);
                examUser.setScore(score);
                examUserBiz.update(examUser);
            }

        }
    }
}

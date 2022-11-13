package com.nextrt.acm.service.oj.user;


import com.nextrt.acm.biz.contest.ContestBiz;
import com.nextrt.acm.biz.contest.ContestJoinInfoBiz;
import com.nextrt.acm.biz.exercise.SubmissionBiz;
import com.nextrt.acm.service.SubmissionService;
import com.nextrt.core.entity.contest.Contest;
import com.nextrt.core.entity.contest.ContestJoinInfo;
import com.nextrt.core.entity.exercise.Submission;
import com.nextrt.core.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.nextrt.acm.util.NetUtil.checkContestIP;

@Service
public class UserSubmissionService {
    @Autowired
    private SubmissionBiz submissionBiz;
    @Autowired
    private ContestBiz contestBiz;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private ContestJoinInfoBiz contestJoinInfoBiz;

    public Result contestSubmission(Submission submission) {
        Contest contest = contestBiz.examGetContestInfo(submission.getContestId());
        ContestJoinInfo contestJoinInfo = contestJoinInfoBiz.getContestJoinInfo(submission.getContestId(),submission.getUserId());
        if (contest == null) return new Result(-1, " 不存在该竞赛!");
        if(contestJoinInfo == null)
            return new Result(-1,"你无权限参与该竞赛");
        if (checkContestIP(submission.getPublicIp(), contest.getPublicIpRemark()) == 0)
            return new Result(-1, "您当前的公网IP不在允许范围内!");
        List<String> language = Arrays.asList(contest.getLanguage().split(","));
        if (!language.contains(submission.getLanguage()))
            return new Result(-1, "当前语言不被支持");
        if (contestJoinInfo.getStartTime() == null)
            return new Result(-1, "您没开始竞赛无法提交");
        if (System.currentTimeMillis() - 60 * 1000 > contestJoinInfo.getEndTime().getTime())
            return new Result(-1, "竞赛已经截止，无法提交答案！");
        if (submissionService.addSubmission(submission) == 1) {
            return new Result(1, "题目提交成功,正在判题!");
        } else {
            return new Result(-1, "系统出错请联系管理员!");
        }
    }

    public Result getContestUserSubmission(int contestId, int problemId, int userId, int page, int size) {
        return new Result(1, "获取成功", submissionBiz.contestGetUserSubmission(contestId, problemId, userId, page, size));
    }

    public Result getContestAllSubmission(int contestId, int problemId, int page, int size) {
        return new Result(1, "获取成功", submissionBiz.contestGetAllSubmission(contestId, problemId, page, size));
    }

    //用户根据题目查看自己的查看提交信息
    public Result getOrdinaryUserSubmission(int problemId, int userId, int page, int size) {
        return new Result(1, "获取成功", submissionBiz.getOrdinaryUserSubmission(problemId, userId, page, size));
    }
}

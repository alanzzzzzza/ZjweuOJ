package com.nextrt.acm.service.oj.admin;

import com.nextrt.acm.biz.contest.ContestBiz;
import com.nextrt.acm.biz.exercise.SubmissionBiz;
import com.nextrt.core.entity.contest.Contest;
import com.nextrt.core.vo.Result;
import org.springframework.stereotype.Service;

@Service
public class AdminSubmissionService {
    private final SubmissionBiz submissionBiz;
    private final ContestBiz contestBiz;

    public AdminSubmissionService(SubmissionBiz submissionBiz, ContestBiz contestBiz) {
        this.submissionBiz = submissionBiz;
        this.contestBiz = contestBiz;
    }

    public Result getUserSubmission(int contestId, int problemId, int userId) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest.getIsTestAccount() == 1)
            return new Result(1, "获取成功", submissionBiz.examGetUserSubmission(contestId, problemId, userId));
        else
            return new Result(1, "获取成功", submissionBiz.contestGetUserSubmission(contestId, problemId, userId));
    }

    public Result getAllSubmission(int contestId, int problemId) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest.getIsTestAccount() == 1)
            return new Result(1, "获取成功", submissionBiz.examGetAllSubmission(contestId, problemId));
        else
            return new Result(1, "获取成功", submissionBiz.contestGetAllSubmission(contestId, problemId));
    }


    public Result getUserSubmission(int contestId, int problemId, int userId, int page, int size) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest.getIsTestAccount() == 1)
            return new Result(1, "获取成功", submissionBiz.examGetUserSubmission(contestId, problemId, userId, page, size));
        else
            return new Result(1, "获取成功", submissionBiz.contestGetUserSubmission(contestId, problemId, userId, page, size));
    }

    public Result getAllSubmission(int contestId, int problemId, int page, int size) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest.getIsTestAccount() == 1)
            return new Result(1, "获取成功", submissionBiz.examGetAllSubmission(contestId, problemId, page, size));
        else
            return new Result(1, "获取成功", submissionBiz.contestGetAllSubmission(contestId, problemId, page, size));
    }
}

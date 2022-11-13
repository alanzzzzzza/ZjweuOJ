package com.nextrt.acm.service.oj.teacher;

import com.nextrt.acm.biz.contest.ContestBiz;
import com.nextrt.acm.biz.exercise.SubmissionBiz;
import com.nextrt.core.entity.contest.Contest;
import com.nextrt.core.vo.Result;
import org.springframework.stereotype.Service;

@Service
public class TeacherSubmissionService {
    private final SubmissionBiz submissionBiz;
    private final ContestBiz contestBiz;

    public TeacherSubmissionService(SubmissionBiz submissionBiz, ContestBiz contestBiz) {
        this.submissionBiz = submissionBiz;
        this.contestBiz = contestBiz;
    }

    public Result getUserSubmission(int contestId, int userId, int problemId, int getUserId) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest.getUserId() != userId) return new Result(-1, "权限不足，请确认该竞赛创建人是本帐号");
        if (contest.getIsTestAccount() == 1)
            return new Result(1, "获取成功", submissionBiz.examGetUserSubmission(contestId, problemId, getUserId));
        else
            return new Result(1, "获取成功", submissionBiz.contestGetUserSubmission(contestId, problemId, getUserId));
    }

    public Result getAllSubmission(int contestId, int userId, int problemId) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest.getUserId() != userId) return new Result(-1, "权限不足，请确认该竞赛创建人是本帐号");

        if (contest.getIsTestAccount() == 1)
            return new Result(1, "获取成功", submissionBiz.examGetAllSubmission(contestId, problemId));
        else
            return new Result(1, "获取成功", submissionBiz.contestGetAllSubmission(contestId, problemId));
    }


    public Result getUserSubmission(int contestId, int userId, int problemId, int getUserId, int page, int size) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest.getUserId() != userId) return new Result(-1, "权限不足，请确认该竞赛创建人是本帐号");
        if (contest.getIsTestAccount() == 1)
            return new Result(1, "获取成功", submissionBiz.examGetUserSubmission(contestId, problemId, getUserId, page, size));
        else
            return new Result(1, "获取成功", submissionBiz.contestGetUserSubmission(contestId, problemId, getUserId, page, size));
    }

    public Result getAllSubmission(int contestId, int userId, int problemId, int page, int size) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest.getUserId() != userId) return new Result(-1, "权限不足，请确认该竞赛创建人是本帐号");
        if (contest.getIsTestAccount() == 1)
            return new Result(1, "获取成功", submissionBiz.examGetAllSubmission(contestId, problemId, page, size));
        else
            return new Result(1, "获取成功", submissionBiz.contestGetAllSubmission(contestId, problemId, page, size));
    }
}

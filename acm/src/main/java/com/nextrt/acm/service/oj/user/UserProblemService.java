package com.nextrt.acm.service.oj.user;

import com.nextrt.acm.biz.exercise.ProblemBiz;
import com.nextrt.core.entity.exercise.Problem;
import com.nextrt.core.vo.Result;
import org.springframework.stereotype.Service;

@Service
public class UserProblemService {

    private final ProblemBiz problemBiz;

    public UserProblemService(ProblemBiz problemBiz) {
        this.problemBiz = problemBiz;
    }

    public Result userGetProblems(int difficulty, int userId, String name, int page, int size) {
        return new Result(1, "题目信息获取成功！", problemBiz.userGetProblemsList(difficulty, userId, name, page, size));
    }

    public Result userGetProblemById(int problemId) {
        Problem problem = problemBiz.getProblemById(problemId);
        if (problem == null)
            return new Result(-1, "问题不存在，或者您没有权限访问该题目");
        else
            return new Result(1, "问题信息获取成功", problem);
    }
}

package com.nextrt.acm.controller.user;

import cn.hutool.core.util.StrUtil;
import com.nextrt.acm.service.SubmissionService;
import com.nextrt.acm.service.oj.user.UserSubmissionService;
import com.nextrt.core.vo.Result;
import com.nextrt.core.entity.exercise.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.nextrt.acm.util.NetUtil.getPublicIP;

@RestController
@RequestMapping(value = "/user/submission", produces="application/json;charset=UTF-8")
public class SubmissionUserController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private UserSubmissionService userSubmissionService;

    @PostMapping("add")
    public Result addSubmission(@RequestBody Submission submission, HttpServletRequest request){
        submission.setContestId(0);
        if(submission.getProblemId() <= 0) return new Result(-2,"问题编号不能为空!");
        if(StrUtil.hasBlank(submission.getCode())) return new Result(-3,"提交的代码不能为空！");
        submission.setPublicIp(getPublicIP(request));
        submission.setUserId(request.getIntHeader("userId"));
        submission.setContestId(0);
        int status = submissionService.addSubmission(submission);
        switch (status){
            case -1:
                return new Result(-1,"该问题系统不存在，请检查后再试！");
            case 1:
                return new Result(1,"代码提交成功，系统正在判题，请稍等！");
            default:
                return new Result(0,"系统出错，请联系管理员!");
        }
    }

    @GetMapping("ordinary")
    public Result ordinaryUserSubmission(@RequestParam int problemId,@RequestParam int page,@RequestParam int size, HttpServletRequest request){
        int userId = request.getIntHeader("userId");
        return userSubmissionService.getOrdinaryUserSubmission(problemId, userId, page, size);
    }

    @GetMapping("contest")
    public Result contestSubmissionList(@RequestParam int problemId,@RequestParam int contestId,@RequestParam int page,@RequestParam int size, HttpServletRequest request){
        int userId = request.getIntHeader("userId");
        return userSubmissionService.getContestUserSubmission(contestId,problemId,userId,page,size);
    }

    @GetMapping("contest/all")
    public Result GetContestSubmission(@RequestParam int problemId,@RequestParam int contestId,@RequestParam int page,@RequestParam int size, HttpServletRequest request){
        return userSubmissionService.getContestAllSubmission(contestId,problemId,page,size);
    }

}

package com.nextrt.acm.controller.user;


import cn.hutool.core.util.StrUtil;
import com.nextrt.acm.service.oj.user.UserContestService;
import com.nextrt.core.entity.contest.ContestIPReport;
import com.nextrt.core.entity.contest.ContestJoinInfo;
import com.nextrt.core.entity.exercise.Submission;
import com.nextrt.core.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.nextrt.acm.util.NetUtil.getPublicIP;

@RestController
@RequestMapping(value = "/user/contest", produces = "application/json;charset=UTF-8")
public class ContestUserController {
    private final UserContestService userContestService;

    public ContestUserController(UserContestService userContestService) {
        this.userContestService = userContestService;
    }

    @GetMapping("list")
    public Result getContests(String name, int page, int size) {
        return userContestService.getContestList(name, page, size);
    }

    @GetMapping("my/join")
    public Result getContests(int page, int size, HttpServletRequest request) {
        return userContestService.getJoinContestList(request.getIntHeader("userId"), page, size);
    }

    @PostMapping(value = "/join")
    public Result addContest(@RequestBody ContestJoinInfo contestJoinInfo, HttpServletRequest request) {
        contestJoinInfo.setUserId(request.getIntHeader("userId"));
        contestJoinInfo.setJoinPublicIp(getPublicIP(request));
        return userContestService.addContestJoinInfo(contestJoinInfo);
    }

    @PostMapping(value = "/report")
    public Result ipReport(@RequestBody @Valid ContestIPReport contestIPReport, HttpServletRequest request) {
        contestIPReport.setUserId(request.getIntHeader("userId"));
        contestIPReport.setPublicIp(getPublicIP(request));
        if (contestIPReport.getContestId() == null || contestIPReport.getContestId() < 1)
            return new Result(-1, "error");
        return userContestService.reportIP(contestIPReport);
    }

    @GetMapping(value = "get/{id:[0-9]+}")
    public Result getContestInfo(@PathVariable int id, @RequestParam String localIP, HttpServletRequest request) {
        return userContestService.userGetContestInfo(id, request.getIntHeader("userId"),localIP, getPublicIP(request));
    }

    @GetMapping(value = "problem/{id:[0-9]+}")
    public Result getContestProblems(@PathVariable int id, HttpServletRequest request) {
        return userContestService.userGetProblemsByContestId(id, request.getIntHeader("userId"), getPublicIP(request));
    }
    @GetMapping(value = "start/{contestId:[0-9]+}")
    public Result startContest(@PathVariable int contestId, @RequestParam String localIP, HttpServletRequest request) {
        return userContestService.startContest(contestId, request.getIntHeader("userId"), localIP, getPublicIP(request));
    }
    @PostMapping("submission")
    public Result contestSubmission(@RequestBody Submission submission, HttpServletRequest request) {
        if (submission.getContestId() <= 0)
            return new Result(-2, "竞赛ID，不能为空!");
        if (submission.getProblemId() <= 0)
            return new Result(-2, "问题编号不能为空!");
        if (StrUtil.hasBlank(submission.getCode()))
            return new Result(-3, "提交的代码不能为空！");
        submission.setPublicIp(getPublicIP(request));
        submission.setUserId(request.getIntHeader("userId"));
        return userContestService.contestSubmission(submission);
    }

    @GetMapping(value = "result/{contestId:[0-9]+}")
    public Result getContestResult(@PathVariable int contestId, HttpServletRequest request) {
        return userContestService.getContestResult(contestId,request.getIntHeader("userId"),getPublicIP(request));
    }

    @GetMapping(value = "rank/{contestId:[0-9]+}")
    public Result getContestRank(@PathVariable int contestId, HttpServletRequest request) {
        return userContestService.getContestRank(contestId,request.getIntHeader("userId"),getPublicIP(request));
    }
}

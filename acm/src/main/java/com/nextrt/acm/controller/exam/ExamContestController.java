package com.nextrt.acm.controller.exam;


import cn.hutool.core.util.StrUtil;
import com.nextrt.acm.service.oj.exam.ExamContestService;
import com.nextrt.acm.service.oj.exam.ExamSubmissionService;
import com.nextrt.core.entity.contest.ContestIPReport;
import com.nextrt.core.entity.exercise.Submission;
import com.nextrt.core.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.nextrt.acm.util.NetUtil.getPublicIP;

@RestController
@RequestMapping(value = "/exam/contest", produces = "application/json;charset=UTF-8")
public class ExamContestController {
    private final ExamContestService examContestService;
    private final ExamSubmissionService examSubmissionService;

    public ExamContestController(ExamContestService examContestService, ExamSubmissionService examSubmissionService) {
        this.examContestService = examContestService;
        this.examSubmissionService = examSubmissionService;
    }

    @PostMapping("/start")
    public Result addContest(String localIP, HttpServletRequest request) {
        return examContestService.start(request.getIntHeader("contestId"), request.getIntHeader("userId"), localIP, getPublicIP(request));
    }

    @PostMapping("/report")
    public Result ipReport(@RequestBody @Valid ContestIPReport contestIPReport, HttpServletRequest request) {
        contestIPReport.setUserId(request.getIntHeader("userId"));
        contestIPReport.setContestId(request.getIntHeader("contestId"));
        contestIPReport.setPublicIp(getPublicIP(request));
        if (contestIPReport.getContestId() == null || contestIPReport.getContestId() < 1)
            return new Result(-1, "error");
        return examContestService.reportIP(contestIPReport);
    }

    @GetMapping("/info")
    public Result getContestInfo(HttpServletRequest request) {
        return examContestService.getContestInfo(request.getIntHeader("contestId"), request.getIntHeader("userId"), getPublicIP(request));
    }

    @GetMapping("/problem")
    public Result getContestProblems(HttpServletRequest request) {
        return examContestService.getProblemsByContestId(request.getIntHeader("contestId"), request.getIntHeader("userId"), getPublicIP(request));
    }

    @PostMapping("/submission")
    public Result contestSubmission(@RequestBody Submission submission, HttpServletRequest request) {
        if (submission.getProblemId() <= 0)
            return new Result(-2, "问题编号不能为空!");
        if (StrUtil.hasBlank(submission.getCode()))
            return new Result(-3, "提交的代码不能为空！");
        submission.setContestId(request.getIntHeader("contestId"));
        submission.setPublicIp(getPublicIP(request));
        submission.setUserId(request.getIntHeader("userId"));
        return examSubmissionService.contestSubmission(submission);
    }

    @GetMapping("/result")
    public Result getContestResult(HttpServletRequest request) {
        return examContestService.getContestResult(request.getIntHeader("userId"), request.getIntHeader("contestId"));
    }

    @GetMapping("/rank")
    public Result getContestRank(HttpServletRequest request) {
        return examContestService.getContestRank(request.getIntHeader("contestId"));
    }

    @GetMapping("/submission")
    public Result contestSubmissionList(@RequestParam int problemId, @RequestParam int page, @RequestParam int size, HttpServletRequest request) {
        return examSubmissionService.getUserSubmission(request.getIntHeader("contestId"), problemId, request.getIntHeader("userId"), page, size);
    }

    @GetMapping("/allsubmission")
    public Result GetContestSubmission(@RequestParam int page, @RequestParam int size, HttpServletRequest request) {
        return examSubmissionService.getAllSubmission(request.getIntHeader("contestId"), -1, page, size);
    }

}

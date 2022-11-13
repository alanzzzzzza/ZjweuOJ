package com.nextrt.acm.controller.teacher;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.nextrt.acm.service.oj.teacher.TeacherContestService;
import com.nextrt.core.entity.contest.Contest;
import com.nextrt.core.entity.contest.ContestIPReport;
import com.nextrt.core.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/teacher/contest", produces = "application/json;charset=UTF-8")
public class TeacherContestController {
    private final TeacherContestService teacherContestService;

    public TeacherContestController(TeacherContestService teacherContestService) {
        this.teacherContestService = teacherContestService;
    }

    @PostMapping(value = "/add")
    public Result addContest(@RequestBody @Valid Contest contest, HttpServletRequest request) {
        if (contest.getEndTime().getTime() <= contest.getStartTime().getTime())
            return new Result(-2, "竞赛结束时间不能小于竞赛开始时间！");
        if (checkIP(contest) == 2) {
            return new Result(-2, "PublicIP 填写有误！");
        }
        contest.setUserId(request.getIntHeader("userId"));
        if (StrUtil.hasBlank(contest.getPassword()))
            contest.setIsPublic(1);
        else
            contest.setIsPublic(0);
        return teacherContestService.addContest(contest);
    }

    @GetMapping(value = "/get/{contestId:[0-9]+}")
    public Result getContest(@PathVariable int contestId, HttpServletRequest request) {
        return teacherContestService.getContestInfo(contestId, request.getIntHeader("userId"));
    }

    @PostMapping(value = "/update")
    public Result updateContest(@RequestBody Contest contest, HttpServletRequest request) {
        if (contest.getEndTime().getTime() <= contest.getStartTime().getTime())
            return new Result(-2, "竞赛结束时间不能小于竞赛开始时间！");
        if (checkIP(contest) == 2) {
            return new Result(-2, "PublicIP 填写有误！");
        }
        contest.setUserId(request.getIntHeader("userId"));
        if (StrUtil.hasBlank(contest.getPassword()))
            contest.setIsPublic(1);
        else
            contest.setIsPublic(0);
        return teacherContestService.updateContest(contest);
    }

    @DeleteMapping(value = "/delete/{contestId:[0-9]+}")
    public Result deleteContest(@PathVariable int contestId, HttpServletRequest request) {
        return teacherContestService.deleteContest(contestId, request.getIntHeader("userId"));
    }

    @PostMapping(value = "/copy/{contestId:[0-9]+}")
    public Result copyContest(@PathVariable int contestId, HttpServletRequest request) {
        return teacherContestService.copyContest(contestId, request.getIntHeader("userId"));
    }

    @GetMapping("/list")
    public Result getContests(String name, int page, int size, HttpServletRequest request) {
        return teacherContestService.getContestList(name, request.getIntHeader("userId"), page, size);
    }

    @PostMapping("/rejudge")
    public Result reJudge(int contestId, int problemId, HttpServletRequest request) {
        return teacherContestService.reJudge(contestId, problemId, request.getIntHeader("userId"));
    }

    @GetMapping("/joins")
    public Result getContestJoins(int contestId, int page, int size, HttpServletRequest request) {
        return teacherContestService.getContestJoinList(page, size, request.getIntHeader("userId"), contestId);
    }

    @GetMapping(value = "user/ip/{userId:[0-9]+}/{contestId:[0-9]+}")
    public void getUserIPReportList(@PathVariable int userId, @PathVariable int contestId, HttpServletResponse response, HttpServletRequest request) {
        List<ContestIPReport> list = teacherContestService.getUserIPReportList(contestId, request.getIntHeader("userId"), userId);
        if (!list.isEmpty()) {
            ExcelWriter writer = ExcelUtil.getWriter();
            writer.write(list, true);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=test.xls");
            ServletOutputStream out = null;
            try {
                out = response.getOutputStream();
                writer.flush(out, true);
                writer.close();
                IoUtil.close(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/result/{contestId:[0-9]+}/excel")
    public void getContestsToExcel(@PathVariable int contestId, HttpServletResponse response, HttpServletRequest request) {
        List<Map<String, Object>> list = teacherContestService.getContestResultExcel(contestId, request.getIntHeader("userId"));
        if (!list.isEmpty()) {
            ExcelWriter writer = ExcelUtil.getWriter();
            writer.write(list, true);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=test.xls");
            ServletOutputStream out = null;
            try {
                out = response.getOutputStream();
                writer.merge(list.get(0).size() - 1, "竞赛数据结果汇总表");
                writer.flush(out, true);
                writer.close();
                IoUtil.close(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/result/{contestId:[0-9]+}")
    public Result getContests(@PathVariable int contestId, HttpServletRequest request) {
        List<Map<String, Object>> list = teacherContestService.getContestResultExcel(contestId, request.getIntHeader("userId"));
        return new Result(1, "获取成功", list);
    }

    @GetMapping(value = "problem/{contestId:[0-9]+}")
    public Result getContestProblems(@PathVariable int contestId, HttpServletRequest request) {
        return teacherContestService.getProblemsByContestId(contestId, request.getIntHeader("userId"));
    }

    @GetMapping(value = "user/result/{userId:[0-9]+}/{contestId:[0-9]+}")
    public Result getUserContestResult(@PathVariable int userId, @PathVariable int contestId, HttpServletRequest request) {
        return teacherContestService.getUserContestResult(contestId, request.getIntHeader("userId"), userId);
    }

    @GetMapping(value = "rank/{contestId:[0-9]+}")
    public Result getContestRank(@PathVariable int contestId, HttpServletRequest request) {
        return teacherContestService.getContestRank(contestId, request.getIntHeader("userId"));
    }

    private int checkIP(Contest contest) {
        if (!StrUtil.hasBlank(contest.getPublicIpRemark())) {
            String[] pip = contest.getPublicIpRemark().split("/");
            if (pip.length != 2) return 2;//localIp填写错误
            try {
                if (!Validator.isIpv4(pip[0]) || Integer.parseInt(pip[1]) > 32) return 2;
            } catch (NumberFormatException e) {
                return 2;
            }
        }
        return 0;
    }
}

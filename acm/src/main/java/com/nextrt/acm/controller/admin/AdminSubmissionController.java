package com.nextrt.acm.controller.admin;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.acm.service.SubmissionService;
import com.nextrt.acm.service.oj.admin.AdminSubmissionService;
import com.nextrt.core.entity.exercise.Submission;
import com.nextrt.core.vo.Result;
import com.nextrt.core.vo.contest.SubmissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.nextrt.acm.util.NetUtil.getPublicIP;


@RestController
@RequestMapping(value = "/admin/submission", produces = "application/json;charset=UTF-8")
public class AdminSubmissionController {

    private final AdminSubmissionService adminGetUserSubmission;

    @Autowired
    private SubmissionService submissionService;

    public AdminSubmissionController(AdminSubmissionService adminGetUserSubmission) {
        this.adminGetUserSubmission = adminGetUserSubmission;
    }

    @GetMapping("/export/{contestId:[0-9]+}")
    public void getContestSubmission(@PathVariable int contestId, HttpServletResponse response) {
        List<Object> list = (List<Object>) adminGetUserSubmission.getAllSubmission(contestId,0).getData();
        if (!list.isEmpty()) {
            ExcelWriter writer = ExcelUtil.getWriter();
            writer.write(list, true);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=SubmissionResult.xls");
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
    @GetMapping("contest/{userId:[0-9]+}")
    public Result contestSubmissionList(@PathVariable int userId, @RequestParam int problemId, @RequestParam int contestId, @RequestParam int page, @RequestParam int size) {
        return adminGetUserSubmission.getUserSubmission(contestId, problemId, userId, page, size);
    }

    @GetMapping("contest/all")
    public Result contestAllSubmissionList(@RequestParam int problemId, @RequestParam int contestId, @RequestParam int page, @RequestParam int size) {
        return adminGetUserSubmission.getAllSubmission(contestId, problemId, page, size);
    }

    @PostMapping("rejudge")
    public Result rejudgeSubmission(@RequestBody Submission submission, HttpServletRequest request) {
        if(submission.getProblemId() <= 0) return new Result(-2,"问题编号不能为空!");
        if(StrUtil.hasBlank(submission.getCode())) return new Result(-3,"提交的代码不能为空！");
        int status = submissionService.updateSubmission(submission);
        return new Result(1,"代码提交成功，系统正在判题，请稍等！");
    }

    @PostMapping("rejudge/all")
    public Result rejudgeSubmissionAll(@RequestParam int problemId) {
        IPage<SubmissionVO> submissionVOIPage = submissionService.AdminGetSubmission(problemId, 0,0, Integer.MAX_VALUE);
        for (SubmissionVO submission : submissionVOIPage.getRecords()) {
            submissionService.updateSubmission(submission);
        }
        return new Result(1,"代码提交成功，系统正在判题，请稍等！");
    }

    @GetMapping("list")
    public Result submissionLists(@RequestParam Integer userId, @RequestParam int problemId, @RequestParam int page, @RequestParam int size, HttpServletRequest request){
        IPage<SubmissionVO> data = submissionService.AdminGetSubmission(problemId, userId, page, size);
        if(data == null)
            return new Result(0,"系统不存在数据！");
        else
            return new Result(1,"答题数据获取成功！",data);
    }
}

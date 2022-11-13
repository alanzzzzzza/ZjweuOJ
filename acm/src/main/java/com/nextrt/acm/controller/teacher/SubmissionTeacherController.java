package com.nextrt.acm.controller.teacher;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.nextrt.acm.service.oj.admin.AdminSubmissionService;
import com.nextrt.acm.service.oj.teacher.TeacherSubmissionService;
import com.nextrt.core.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(value = "teacher/submission", produces = "application/json;charset=UTF-8")
public class SubmissionTeacherController {

    private final TeacherSubmissionService teacherSubmissionService;

    public SubmissionTeacherController(TeacherSubmissionService teacherSubmissionService) {
        this.teacherSubmissionService = teacherSubmissionService;
    }

    @GetMapping("/export/{contestId:[0-9]+}")
    public void getContestSubmission(@PathVariable int contestId, HttpServletResponse response, HttpServletRequest request) {
        List<Object> list = (List<Object>) teacherSubmissionService.getAllSubmission(contestId,request.getIntHeader("userId"),0).getData();
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
    public Result contestSubmissionList(@PathVariable int userId, @RequestParam int problemId, @RequestParam int contestId, @RequestParam int page, @RequestParam int size, HttpServletRequest request) {
        return teacherSubmissionService.getUserSubmission(contestId,request.getIntHeader("userId"), problemId, userId, page, size);
    }

    @GetMapping("contest/all")
    public Result contestAllSubmissionList(@RequestParam int problemId, @RequestParam int contestId, @RequestParam int page, @RequestParam int size, HttpServletRequest request) {
        return teacherSubmissionService.getAllSubmission(contestId,request.getIntHeader("userId"), problemId, page, size);

    }
}

package com.nextrt.acm.controller.teacher;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.nextrt.acm.service.oj.teacher.TeacherExamUserService;
import com.nextrt.core.entity.exam.ExamUser;
import com.nextrt.core.vo.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/teacher/exam/user", produces = "application/json;charset=UTF-8")
public class TeacherExamUserController {

    private final TeacherExamUserService userService;

    public TeacherExamUserController(TeacherExamUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/page")
    public Result getContestTestUserList(int page, int size, int contestId, HttpServletRequest request) {
        return userService.getExamUserPage(contestId, request.getIntHeader("userId"), page, size);
    }

    @PostMapping("/add")
    public Result addContestTestUser(Integer contestId, String schoolNoPrefix, String school, String team, String className, Integer num, HttpServletRequest request) {
        return userService.addExamUser(contestId, request.getIntHeader("userId"), schoolNoPrefix, school, team, className, num);
    }

    @DeleteMapping("/{cuid:[0-9]+}")
    public Result deleteUser(@PathVariable Integer cuid, HttpServletRequest request) {
        return userService.deleteExamUserById(cuid, request.getIntHeader("userId"));
    }

    @DeleteMapping("/clean/{contestId:[0-9]+}")
    public Result cleanUser(@PathVariable Integer contestId, HttpServletRequest request) {
        return userService.deleteExamUserByContestId(contestId, request.getIntHeader("userId"));
    }

    @PostMapping("/add/{contestId:[0-9]+}")
    public Result addContestTestUserByExcel(@RequestParam("file") MultipartFile file, @PathVariable int contestId, HttpServletRequest request) {
        return userService.addExamUserByExcel(file, contestId, request.getIntHeader("userId"));
    }

    @GetMapping("/export/{contestId:[0-9]+}")
    public void getContestTestUserToExcel(@PathVariable int contestId, HttpServletResponse response, HttpServletRequest request) {
        List<ExamUser> list = userService.exportExamUser(contestId, request.getIntHeader("userId"));
        if (list != null && !list.isEmpty()) {
            ExcelWriter writer = ExcelUtil.getWriter();
            writer.write(list, true);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=TestUser.xls");
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
}

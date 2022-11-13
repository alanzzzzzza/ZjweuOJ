package com.nextrt.acm.controller.admin;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.nextrt.acm.service.oj.admin.AdminExamUserService;
import com.nextrt.core.entity.exam.ExamUser;
import com.nextrt.core.vo.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/admin/exam/user", produces = "application/json;charset=UTF-8")
public class AdminExamUserController {

    private final AdminExamUserService userService;

    public AdminExamUserController(AdminExamUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/page")
    public Result getContestTestUserList(int page, int size, int contestId) {
        return userService.getExamUserPage(contestId, page, size);
    }

    @PostMapping("/add")
    public Result addContestTestUser(Integer contestId, String schoolNoPrefix, String school, String team, String className, Integer num) {
        return userService.addExamUser(contestId, schoolNoPrefix, school, team, className, num);
    }

    @DeleteMapping("/{cuid:[0-9]+}")
    public Result deleteUser(@PathVariable Integer cuid) {
        return userService.deleteExamUserById(cuid);
    }

    @DeleteMapping("/clean/{contestId:[0-9]+}")
    public Result cleanUser(@PathVariable Integer contestId) {
        return userService.deleteExamUserByContestId(contestId);
    }

    @PostMapping("/add/{contestId:[0-9]+}")
    public Result addContestTestUserByExcel(@RequestParam("file") MultipartFile file, @PathVariable int contestId) {
        return userService.addExamUserByExcel(file, contestId);
    }

    @GetMapping("/export/{contestId:[0-9]+}")
    public void getContestTestUserToExcel(@PathVariable int contestId, HttpServletResponse response) {
        List<ExamUser> list = userService.exportExamUser(contestId);
        if (!list.isEmpty()) {
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

package com.nextrt.acm.controller.teacher;

import com.nextrt.acm.service.oj.teacher.TeacherOrdinaryService;
import com.nextrt.core.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/teacher/ordinary/user", produces = "application/json;charset=UTF-8")
public class TeacherOrdinaryUserController {
    private final TeacherOrdinaryService teacherOrdinaryService;

    public TeacherOrdinaryUserController(TeacherOrdinaryService teacherOrdinaryService) {
        this.teacherOrdinaryService = teacherOrdinaryService;
    }

    @PostMapping("/add")
    public Result addContestTestUser(String data, int type, int contestId, HttpServletRequest request) {
        return teacherOrdinaryService.addJoinUser(data, type, request.getIntHeader("userId"), contestId);
    }

    @DeleteMapping("/{contestId:[0-9]+}/{userId:[0-9]+}")
    public Result deleteUser(@PathVariable Integer contestId, @PathVariable Integer userId, HttpServletRequest request) {
        return teacherOrdinaryService.deleteUserJoin(request.getIntHeader("userId"), contestId, userId);
    }

}

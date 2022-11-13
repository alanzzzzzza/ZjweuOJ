package com.nextrt.acm.controller.user;

import com.nextrt.acm.service.oj.user.UserProblemService;
import com.nextrt.core.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/user/problem", produces = "application/json;charset=UTF-8")
public class ProblemUserController {
    @Autowired
    UserProblemService problemService;

    @GetMapping("/{id:[0-9]+}")
    public Result getProblemById(@PathVariable int id) {
        return problemService.userGetProblemById(id);
    }

    @GetMapping("/list")
    public Result getProblemsByUser(@RequestParam(required = false) int difficulty, @RequestParam(required = false) String name, int page, int size, HttpServletRequest request) {
        return problemService.userGetProblems(difficulty,request.getIntHeader("userId"), name, page, size);
    }
}

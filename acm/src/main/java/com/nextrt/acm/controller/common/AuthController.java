package com.nextrt.acm.controller.common;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.nextrt.acm.service.oj.exam.ExamUserAuthService;
import com.nextrt.core.entity.exam.ExamUser;
import com.nextrt.core.vo.Result;
import com.nextrt.core.entity.user.User;
import com.nextrt.acm.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.nextrt.acm.util.NetUtil.getPublicIP;

@RestController
@RequestMapping(value = "/auth", produces = "application/json;charset=UTF-8")
public class AuthController {
    private final UserService userService;
    private final ExamUserAuthService examUserAuthService;

    public AuthController(UserService userService, ExamUserAuthService examUserAuthService) {
        this.userService = userService;
        this.examUserAuthService = examUserAuthService;
    }

    @PostMapping("exam/login")
    public Result examLogin(@RequestBody ExamUser examUser, HttpServletRequest request) {
        examUser.setJoinPublicIp(getPublicIP(request));
        return examUserAuthService.login(examUser);
    }

    @PostMapping("login")
    public Result login(@RequestBody User user, HttpServletRequest request) {
        user.setLoginIp(getPublicIP(request));
        return userService.loginUser(user);
    }

    @PostMapping("register")
    public Result register(@RequestBody @Valid User user, @RequestParam("code") String code, HttpServletRequest request) {
        user.setRegIp(getPublicIP(request));
        if (StrUtil.hasBlank(user.getPassword()))
            return new Result(-6, "密码不能为空!");
        return userService.registerUser(user, code);
    }

    @GetMapping("register")
    public Result sendRegisterEmail(@RequestParam String email, HttpServletRequest request) {
        if (!Validator.isEmail(email))
            return new Result(-3, "邮箱填写错误空！", "");
        return userService.registerUser(email, getPublicIP(request));
    }

    @GetMapping("forgot")
    public Result sendForgotEmail(@RequestParam String email, HttpServletRequest request) {
        if (!Validator.isEmail(email))
            return new Result(-3, "邮箱填写错误空！", "");
        return userService.findPass(email, getPublicIP(request));
    }

    @PostMapping("forgot")
    public Result forgotPassword(@RequestBody User user, @RequestParam String code, HttpServletRequest request) {
        if (StrUtil.hasBlank(user.getPassword()))
            return new Result(-5, "用户密码不能为空！");
        if (!Validator.isEmail(user.getEmail()))
            return new Result(-4, "邮箱格式错误！");
        return userService.findPass(user, code, getPublicIP(request));
    }
}

package com.nextrt.acm.controller.user;

import cn.hutool.core.util.StrUtil;
import com.nextrt.core.vo.Result;
import com.nextrt.core.entity.user.User;
import com.nextrt.acm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.nextrt.acm.util.NetUtil.getPublicIP;

@RestController
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public Result userInfo(HttpServletRequest request) {
        User user = userService.userInfo(request.getIntHeader("userId"));
        if (user != null)
            return new Result(1, "用户信息获取成功", user);
        return new Result(-1, "用户信息获取失败", "");
    }

    @GetMapping("/log")
    public Result logInfo(HttpServletRequest request) {
        return new Result(1, "信息获取成功", userService.getUserLogInfoList(request.getIntHeader("userId")));
    }

    @PostMapping("update")
    public Result update(@RequestBody User user, HttpServletRequest request) {
        int userid = Integer.parseInt(request.getHeader("userId"));
        if (user.getUserId() != userid)
            return new Result(-1, "权限校验失败！");
        if (StrUtil.hasBlank(user.getNick()))
            return new Result(-4, "昵称不能为空！");
        if (StrUtil.hasBlank(user.getSchool()))
            return new Result(-4, "学校不能为空！");
        user.setLoginIp(getPublicIP(request));
        return userService.updateUser(user);
    }
}

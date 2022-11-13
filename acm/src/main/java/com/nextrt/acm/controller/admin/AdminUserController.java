package com.nextrt.acm.controller.admin;

import com.nextrt.core.vo.Result;
import com.nextrt.core.entity.user.User;
import com.nextrt.acm.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/user", produces = "application/json;charset=UTF-8")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("list")
    public Result adminGetUserList(@RequestParam(required = false) String query, int page, int size) {
        return userService.adminGetUserList(page, size, query);
    }
    @GetMapping("delete/{id:[0-9]+}")
    public Result adminDeleteUserList(@PathVariable int id) {
        return userService.adminDeleteUser(id);
    }
    @PostMapping("update")
    public Result update(@RequestBody User user) {
        return userService.adminUpdateUser(user);
    }
}

package com.nextrt.acm.controller.admin;

import com.nextrt.acm.service.oj.admin.AdminOrdinaryService;
import com.nextrt.core.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/ordinary/user", produces = "application/json;charset=UTF-8")
public class AdminOrdinaryUserController {
    @Autowired
    private AdminOrdinaryService adminOrdinaryService;


    @PostMapping("/add")
    public Result addContestTestUser(String data, int type, int contestId) {
        return adminOrdinaryService.addJoinUser(data,type,contestId);
    }

    @DeleteMapping("/{contestId:[0-9]+}/{userId:[0-9]+}")
    public Result deleteUser(@PathVariable Integer contestId, @PathVariable Integer userId) {
        return adminOrdinaryService.deleteUserJoin(userId, contestId);
    }

}

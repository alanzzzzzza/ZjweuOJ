package com.nextrt.acm.controller.admin;

import com.nextrt.acm.service.JudgeService;
import com.nextrt.core.vo.Result;
import com.nextrt.core.entity.judge.JudgeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/judge", produces = "application/json;charset=UTF-8")
public class AdminJudgeController {
    private final JudgeService judgeService;

    public AdminJudgeController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @GetMapping("/list")
    public Result getServerList() {
        List<JudgeServer> list = judgeService.getJudgeServerList();
        return new Result(1, "获取成功", list);
    }

}

package com.nextrt.acm.controller.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.core.vo.Result;
import com.nextrt.core.entity.common.Help;
import com.nextrt.acm.service.system.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "common/help", produces = "application/json;charset=UTF-8")
public class HelpController {
    @Autowired
    private HelpService helpService;

    @GetMapping("/list")
    public Result getHelp(int page, int num) {
        IPage<Help> helpIPage = helpService.getHelps(page, num);
        if (helpIPage != null)
            return new Result(1, "帮助获取成功！", helpIPage);
        return new Result(0, "系统当前没有帮助！", "");
    }

    @GetMapping("/{id:[0-9]+}")
    public Result getHelpById(@PathVariable("id") int id) {
        Help help = helpService.getHelp(id);
        if (help != null)
            return new Result(1, "帮助获取成功", help);
        return new Result(0, "帮助不存在或者已经被删除！", "");
    }
}

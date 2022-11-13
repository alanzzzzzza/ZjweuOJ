package com.nextrt.acm.controller.teacher;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.acm.service.system.HelpService;
import com.nextrt.core.entity.common.Help;
import com.nextrt.core.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "teacher/help", produces = "application/json;charset=UTF-8")
public class TeacherHelpController {
    private final HelpService helpService;

    public TeacherHelpController(HelpService helpService) {
        this.helpService = helpService;
    }

    @GetMapping("/get/{id:[0-9]+}")
    public Result getHelpById(@PathVariable("id") int id) {
        Help help = helpService.getHelp(id);
        if (help != null)
            return new Result(1, "帮助获取成功", help);
        return new Result(0, "帮助不存在或者已经被删除！", "");
    }

    @GetMapping("/list")
    public Result getHelps(int page, int size) {
        IPage<Help> helpIPage = helpService.getHelps(page, size);
        if (helpIPage != null)
            return new Result(1, "帮助获取成功！", helpIPage);
        return new Result(0, "系统当前没有帮助！", "");
    }
}

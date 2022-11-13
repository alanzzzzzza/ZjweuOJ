package com.nextrt.acm.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.core.vo.Result;
import com.nextrt.core.entity.common.Help;
import com.nextrt.acm.service.system.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "admin/help", produces = "application/json;charset=UTF-8")
public class AdminHelpController {
    @Autowired
    private HelpService helpService;

    @PostMapping("/add")
    public Result addHelp(@RequestBody Help help, HttpServletRequest request) {
        if (StrUtil.hasBlank(help.getTitle()) || StrUtil.hasBlank(help.getContent()))
            return new Result(-1, "标题和内容不能为空，请检查输入!", "");
        Integer userId = request.getIntHeader("userId");
        help.setUserId(userId);
        if (helpService.addHelp(help) == 1)
            return new Result(1, "帮助添加成功", "");
        return new Result(0, "帮助添加失败，请联系管理员", "");
    }

    @PostMapping("/update")
    public Result updateHelp(@RequestBody Help help, HttpServletRequest request) {
        if (StrUtil.hasBlank(help.getTitle()) || StrUtil.hasBlank(help.getContent()))
            return new Result(-1, "标题和内容不能为空，请检查输入!", "");
        if (help.getId() == null || help.getId() == 0)
            return new Result(-2, "帮助ID不能为空", "");
        Integer userId = request.getIntHeader("userId");
        help.setUserId(userId);
        if (helpService.updateHelp(help) == 1)
            return new Result(1, "公告更新成功", "");
        return new Result(0, "公告更新失败，请联系管理员", "");
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

    @GetMapping("/delete")
    public Result deleteHelp(int id) {
        if (helpService.deleteHelpById(id) == 1)
            return new Result(1, "帮助删除成功！", "");
        return new Result(0, "系统删除失败帮助！", "");
    }


}

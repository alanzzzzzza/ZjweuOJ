package com.nextrt.acm.controller.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.acm.service.ChatmentInfoService;
import com.nextrt.core.entity.common.ChatmentInfo;
import com.nextrt.core.vo.Result;
import com.nextrt.core.vo.system.ChatmentInfoVO;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "user/chatment/info", produces = "application/json;charset=UTF-8")
public class ChatmentInfoController {
    private final ChatmentInfoService chatmentInfoService;

    public ChatmentInfoController(ChatmentInfoService chatmentInfoService) {
        this.chatmentInfoService = chatmentInfoService;
    }

    @GetMapping("/list")
    public Result getChatmentInfoByUser(int page, int num, int pid, HttpServletRequest request) {
        IPage<ChatmentInfoVO> chatment = chatmentInfoService.userGetChatments(page, num, pid);
        if (chatment != null)
            return new Result(1, "记录获取成功！", chatment);
        return new Result(0, "系统当前没有记录！", "");
    }

    @PostMapping("/add")
    public Result addChatmentInfo(@RequestBody ChatmentInfo chatmentInfo, HttpServletRequest request) {
        if (StrUtil.hasBlank(chatmentInfo.getContent()))
            return new Result(-1, "标题和内容不能为空，请检查输入!", "");
        Integer userId = request.getIntHeader("userId");
        chatmentInfo.setUserId(userId);
        if (chatmentInfoService.addChatmentInfo(chatmentInfo) == 1)
            return new Result(1, "记录添加成功", "");
        return new Result(0, "记录添加失败，请联系管理员", "");
    }

    @PostMapping("/delete")
    public Result deleteChatmentInfo(int id, HttpServletRequest request) {
        Integer userId = request.getIntHeader("userId");
        ChatmentInfoVO chatmentInfo = chatmentInfoService.getChatmentInfoById(id);
        if(chatmentInfo == null || !chatmentInfo.getUserId().equals(userId)) {
            return new Result(0, "记录删除失败，请联系管理员", "");
        }
        if (chatmentInfoService.deleteChatmentInfoById(id) == 1)
            return new Result(1, "记录删除成功", "");
        return new Result(0, "记录删除失败，请联系管理员", "");
    }
}
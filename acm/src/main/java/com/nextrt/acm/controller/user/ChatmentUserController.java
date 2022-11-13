package com.nextrt.acm.controller.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.acm.service.ChatmentInfoService;
import com.nextrt.acm.service.ChatmentService;
import com.nextrt.core.entity.common.Chatment;
import com.nextrt.core.vo.Result;
import com.nextrt.core.vo.system.ChatmentVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "user/chatment", produces = "application/json;charset=UTF-8")
public class ChatmentUserController {
    private final ChatmentService chatmentService;
    @Resource
    private ChatmentInfoService chatmentInfoService;

    public ChatmentUserController(ChatmentService chatmentService) {
        this.chatmentService = chatmentService;
    }

    @GetMapping("/list")
    public Result getChatmentsByUser(int page, int num, HttpServletRequest request) {
        IPage<ChatmentVO> chatment = chatmentService.userGetChatments(page, num);
        if (chatment != null)
            return new Result(1, "记录获取成功！", chatment);
        return new Result(0, "系统当前没有记录！", "");
    }

    @GetMapping("/{id:[0-9]+}")
    public Result userGetChatmentById(@PathVariable("id") int id, HttpServletRequest request) {
        ChatmentVO chatment = chatmentService.getChatment(id);
        if (chatment != null) {
            return new Result(1, "记录获取成功", chatment);
        }
        return new Result(0, "记录不存在或者已经被删除！", "");
    }

    @PostMapping("/add")
    public Result addChatment(@RequestBody Chatment chatment, HttpServletRequest request) {
        if (StrUtil.hasBlank(chatment.getTitle()) || StrUtil.hasBlank(chatment.getContent()))
            return new Result(-1, "标题和内容不能为空，请检查输入!", "");
        Integer userId = request.getIntHeader("userId");
        chatment.setUserId(userId);
        if (chatmentService.addChatment(chatment) == 1)
            return new Result(1, "记录添加成功", "");
        return new Result(0, "记录添加失败，请联系管理员", "");
    }

    @PostMapping("/delete")
    public Result deleteChatment(int id, HttpServletRequest request) {
        Integer userId = request.getIntHeader("userId");
        ChatmentVO chatment = chatmentService.getChatment(id);
        if(chatment == null || !chatment.getUserId().equals(userId)) {
            return new Result(0, "记录删除失败，请联系管理员", "");
        }
        chatmentService.userGetChatments(1, Integer.MAX_VALUE).getRecords().forEach(e -> chatmentInfoService.deleteChatmentInfoById(e.getId()));
        if (chatmentService.deleteChatmentById(id) == 1)
            return new Result(1, "记录删除成功", "");
        return new Result(0, "记录删除失败，请联系管理员", "");
    }
}
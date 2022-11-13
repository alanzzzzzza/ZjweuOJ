package com.nextrt.acm.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.core.vo.Result;
import com.nextrt.core.vo.system.AnnouncementVO;
import com.nextrt.core.entity.common.Announcement;
import com.nextrt.acm.service.AnnouncementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "user/announcement", produces = "application/json;charset=UTF-8")
public class AnnouncementUserController {
    private final AnnouncementService announcementService;

    public AnnouncementUserController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping("/list")
    public Result getAnnouncementsByUser(int page, int num, HttpServletRequest request) {
        IPage<AnnouncementVO> announcement = announcementService.userGetAnnouncement(page, num, request.getIntHeader("userLevel"));
        if (announcement != null)
            return new Result(1, "公告获取成功！", announcement);
        return new Result(0, "系统当前没有公告！", "");
    }

    @GetMapping("/{id:[0-9]+}")
    public Result userGetAnnouncementById(@PathVariable("id") int id, HttpServletRequest request) {
        Announcement announcement = announcementService.getAnnouncement(id);
        if (announcement != null)
            if (request.getIntHeader("userLevel") < announcement.getShowType())
                return new Result(-1, "权限不足，无法查阅该公告");
            else
                return new Result(1, "公告获取成功", announcement);
        return new Result(0, "公告不存在或者已经被删除！", "");
    }
}

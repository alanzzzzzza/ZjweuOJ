package com.nextrt.acm.controller.teacher;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.acm.service.AnnouncementService;
import com.nextrt.core.entity.common.Announcement;
import com.nextrt.core.vo.Result;
import com.nextrt.core.vo.system.AnnouncementVO;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "teacher/announcement", produces = "application/json;charset=UTF-8")
public class TeacherAnnouncementController {
    private final AnnouncementService announcementService;

    public TeacherAnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping("/get/{id:[0-9]+}")
    public Result getAnnouncementById(@PathVariable("id") int id, HttpServletRequest request) {
        Announcement announcement = announcementService.getAnnouncement(id);
        if (announcement != null)
            if (request.getIntHeader("userLevel") < announcement.getShowType())
                return new Result(-1, "权限不足，无法查阅该公告");
            else
                return new Result(1, "公告获取成功", announcement);
        return new Result(0, "公告不存在或者已经被删除！", "");
    }

    @GetMapping("/list")
    public Result getAnnouncements(int page, int size, HttpServletRequest request) {
        IPage<AnnouncementVO> announcement = announcementService.userGetAnnouncement(page, size, request.getIntHeader("userLevel"));
        if (announcement != null)
            return new Result(1, "公告获取成功！", announcement);
        return new Result(0, "系统当前没有公告！", "");
    }

}

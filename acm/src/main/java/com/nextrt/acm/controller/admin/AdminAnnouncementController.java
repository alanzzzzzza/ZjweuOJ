package com.nextrt.acm.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.core.vo.Result;
import com.nextrt.core.vo.system.AnnouncementVO;
import com.nextrt.core.entity.common.Announcement;
import com.nextrt.acm.service.AnnouncementService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "admin/announcement", produces = "application/json;charset=UTF-8")
public class AdminAnnouncementController {
    private final AnnouncementService announcementService;

    public AdminAnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @PostMapping("/add")
    public Result addAnnouncement(@RequestBody Announcement announcement, HttpServletRequest request) {
        if (StrUtil.hasBlank(announcement.getTitle()) || StrUtil.hasBlank(announcement.getContent()))
            return new Result(-1, "标题和内容不能为空，请检查输入!", "");
        Integer userId = request.getIntHeader("userId");
        announcement.setUserId(userId);
        if (announcementService.addAnnouncement(announcement) == 1)
            return new Result(1, "公告添加成功", "");
        return new Result(0, "公告添加失败，请联系管理员", "");
    }

    @PostMapping("/update")
    public Result updateAnnouncement(@RequestBody Announcement announcement, HttpServletRequest request) {
        if (StrUtil.hasBlank(announcement.getTitle()) || StrUtil.hasBlank(announcement.getContent()))
            return new Result(-1, "标题和内容不能为空，请检查输入!", "");
        if (announcement.getId() == null || announcement.getId() == 0)
            return new Result(-2, "公告ID不能为空", "");
        Integer userId = request.getIntHeader("userId");
        announcement.setUserId(userId);
        if (announcementService.updateAnnouncement(announcement) == 1)
            return new Result(1, "公告更新成功", "");
        return new Result(0, "公告更新失败，请联系管理员", "");
    }

    @GetMapping("/get/{id:[0-9]+}")
    public Result getAnnouncementById(@PathVariable("id") int id) {
        Announcement announcement = announcementService.getAnnouncement(id);
        if (announcement != null)
            return new Result(1, "公告获取成功", announcement);
        return new Result(0, "公告不存在或者已经被删除！", "");
    }

    @GetMapping("/list")
    public Result getAnnouncements(int page, int size, Integer showType) {
        IPage<AnnouncementVO> announcement = announcementService.adminGetAnnouncement(page, size, showType);
        if (announcement != null)
            return new Result(1, "公告获取成功！", announcement);
        return new Result(0, "系统当前没有公告！", "");
    }

    @GetMapping("/delete")
    public Result deleteAnnouncements(int id) {
        if (announcementService.deleteAnnouncementById(id) == 1)
            return new Result(1, "公告删除成功！", "");
        return new Result(0, "系统删除失败公告！", "");
    }


}

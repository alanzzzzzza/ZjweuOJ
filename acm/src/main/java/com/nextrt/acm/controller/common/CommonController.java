package com.nextrt.acm.controller.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.acm.service.SubmissionService;
import com.nextrt.core.vo.Result;
import com.nextrt.core.vo.contest.SubmissionVO;
import com.nextrt.core.vo.system.AnnouncementVO;
import com.nextrt.core.entity.common.Announcement;
import com.nextrt.acm.service.AnnouncementService;
import com.nextrt.acm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "common", produces="application/json;charset=UTF-8")
public class CommonController {

    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private UserService userService;
    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/submission/list")
    public Result submissionLists(@RequestParam Integer userId, @RequestParam int problemId, @RequestParam int page, @RequestParam int size, HttpServletRequest request){
        IPage<SubmissionVO> data = submissionService.TouristsGetSubmission(problemId,userId,page,size);
        if(data == null)
            return new Result(0,"系统不存在数据！");
        else
            return new Result(1,"答题数据获取成功！",data);
    }

    @GetMapping("/rank/list")
    public Result getUserRank(@RequestParam int page,@RequestParam int size){
        return userService.userRank(page,size);
    }

    @GetMapping("announcement/list")
    public Result getAnnouncementsByUser(int page, int num, HttpServletRequest request) {
        IPage<AnnouncementVO> announcement = announcementService.userGetAnnouncement(page, num, 0);
        if (announcement != null)
            return new Result(1, "公告获取成功！", announcement);
        return new Result(0, "系统当前没有公告！", "");
    }

    @GetMapping("announcement/{id:[0-9]+}")
    public Result userGetAnnouncementById(@PathVariable("id") int id, HttpServletRequest request) {
        Announcement announcement = announcementService.getAnnouncement(id);
        int userLevel = request.getIntHeader("userLevel");
        if(userLevel<0) userLevel =0;
        if (announcement != null)
            if (userLevel < announcement.getShowType())
                return new Result(-1, "权限不足，无法查阅该公告");
            else
                return new Result(1, "公告获取成功", announcement);
        return new Result(0, "公告不存在或者已经被删除！", "");
    }
}

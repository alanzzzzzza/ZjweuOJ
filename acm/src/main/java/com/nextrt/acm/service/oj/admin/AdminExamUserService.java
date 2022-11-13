package com.nextrt.acm.service.oj.admin;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nextrt.acm.biz.exam.ExamUserBiz;
import com.nextrt.core.entity.exam.ExamUser;
import com.nextrt.core.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AdminExamUserService {

    private final ExamUserBiz examUserBiz;

    public AdminExamUserService(ExamUserBiz examUserBiz) {
        this.examUserBiz = examUserBiz;
    }

    //删除测试竞赛用户
    public Result deleteExamUserById(int cuid) {
        if (examUserBiz.deleteById(cuid) == 1)
            return new Result(1, "删除成功");
        return new Result(-1, "删除失败");
    }

    //删除测试竞赛用户
    public Result deleteExamUserByContestId(int contestId) {
        if (examUserBiz.deleteByContestId(contestId) > 0)
            return new Result(1, "删除成功");
        return new Result(-1, "删除失败");
    }

    //获取竞赛测试用户
    public Result getExamUserPage(int contestId, int page, int size) {
        return new Result(1, "用户列表获取成功", examUserBiz.getExamUserPageByContestId(contestId, page, size));
    }

    //建立测试帐号
    @Transactional
    public Result addExamUser(int contestId, String schoolNoPrefix, String school, String team, String className, Integer num) {
        String usernamePrefix = RandomUtil.randomStringUpper(8);
        for (int i = 1; i <= num; i++) {
            ExamUser examUser = ExamUser.builder().contestId(contestId).status(0).schoolNo(schoolNoPrefix + i).school(school)
                    .team(team).className(className).username(usernamePrefix + i).password(RandomUtil.randomString(9)).build();
            if (examUserBiz.add(examUser) != 1)
                return new Result(-1, "测试帐号创建失败");
        }
        return new Result(1, "测试帐号创建成功");
    }

    public List<ExamUser> exportExamUser(int contestId) {
        return examUserBiz.getExamUserListByContestId(contestId);
    }

    //通过Excel建立测试帐号
    @Transactional
    public Result addExamUserByExcel(MultipartFile file, int contestId) {
        ExcelReader reader = null;
        try {
            String usernamePrefix = RandomUtil.randomStringUpper(8);
            reader = ExcelUtil.getReader(file.getInputStream());
            List<ExamUser> list = reader.readAll(ExamUser.class);
            AtomicInteger i = new AtomicInteger(1);
            AtomicBoolean flg = new AtomicBoolean(true);
            list.forEach(x -> {
                x.setUsername(usernamePrefix + i);
                x.setStatus(0);
                x.setContestId(contestId);
                if (StrUtil.isBlank(x.getPassword()))
                    x.setPassword(RandomUtil.randomString(9));
                if (examUserBiz.add(x) != 1) flg.set(false);
                i.getAndIncrement();
            });
            if (flg.get()) return new Result(1, "测试帐号创建成功");
            return new Result(-1, "测试帐号创建失败");
        } catch (IOException e) {
            return new Result(-1, "Excel读取失败，请联系管理员");
        }
    }

}

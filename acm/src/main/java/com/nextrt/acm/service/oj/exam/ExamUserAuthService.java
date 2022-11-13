package com.nextrt.acm.service.oj.exam;

import com.nextrt.acm.biz.exam.ExamUserBiz;
import com.nextrt.acm.util.PasswordEncrypt;
import com.nextrt.core.entity.exam.ExamUser;
import com.nextrt.core.vo.Result;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExamUserAuthService {
    private final ExamUserBiz examUserBiz;
    private final PasswordEncrypt encrypt;

    public ExamUserAuthService(PasswordEncrypt encrypt, ExamUserBiz examUserBiz) {
        this.encrypt = encrypt;
        this.examUserBiz = examUserBiz;
    }

    public Result login(ExamUser examUser) {
        ExamUser exitUser = examUserBiz.getContestByUsername(examUser.getUsername());
        if (exitUser == null)
            return new Result(-2, "该竞赛帐号不存在，请检查输入，或者联系竞赛管理员！");
        if(exitUser.getJoinTime() == null)
        {
            exitUser.setJoinTime(new Date());
            exitUser.setJoinPublicIp(examUser.getJoinPublicIp());
            exitUser.setJoinLocalIp(examUser.getJoinLocalIp());
            examUserBiz.update(exitUser);
        }
        if (exitUser.getPassword().equals(examUser.getPassword())) {
            Map<String, Object> data = new HashMap<>();
            data.put("token", encrypt.examUserGetToken(exitUser.getContestUserId(), exitUser.getUsername(),exitUser.getContestId()));
            data.put("userInfo", exitUser);
            return new Result(1, "竞赛帐号登陆成功，欢迎使用！", data);
        }
        return new Result(-1, "密码错误，请检查输入！");
    }
}

package com.nextrt.acm.biz.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nextrt.acm.config.thread.ViThreadPoolManager;
import com.nextrt.core.entity.system.EmailCode;
import com.nextrt.core.mapper.system.EmailCodeMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class EmailBiz {

    private final EmailCodeMapper emailCodeMapper;

    public EmailBiz(EmailCodeMapper emailCodeMapper) {
        this.emailCodeMapper = emailCodeMapper;
    }

    public int insertEmailCode(EmailCode emailCode) {
        return emailCodeMapper.insert(emailCode);
    }

    //获取邮件验证码并置为过期
    public EmailCode getEmailCodeByEmail(String email, String code, String ip) {
        QueryWrapper<EmailCode> emailCodeQueryWrapper = new QueryWrapper<>();
        emailCodeQueryWrapper.lambda().eq(EmailCode::getCode,code).eq(EmailCode::getEmail, email).ge(EmailCode::getExpireTime, new Date()).eq(EmailCode::getStatus, 0);
        emailCodeQueryWrapper.orderByDesc("id").last("limit 1");
        EmailCode emailCode = emailCodeMapper.selectOne(emailCodeQueryWrapper);
        if (emailCode != null) expireEmailCode(email, code, ip);
        return emailCode;
    }

    @Async
    public void expireEmailCode(String email, String code, String ip) {
        ViThreadPoolManager.getInstance().execute(() -> {
            UpdateWrapper<EmailCode> queryWrapper = new UpdateWrapper<>();
            queryWrapper.lambda().eq(EmailCode::getEmail, email).eq(EmailCode::getCode, code).eq(EmailCode::getStatus, 0);
            queryWrapper.orderByDesc("id").last("limit 1");
            queryWrapper.lambda().set(EmailCode::getUseIp, ip).set(EmailCode::getStatus, 1);
            emailCodeMapper.update(null, queryWrapper);
        });
    }

    public int countEmailCode(String email, String ip) {
        QueryWrapper<EmailCode> emailCodeQueryWrapper = new QueryWrapper<>();
        emailCodeQueryWrapper.lambda().ge(EmailCode::getExpireTime, new Date()).eq(EmailCode::getStatus, 0).and(x -> x.eq(EmailCode::getEmail, email).or().eq(EmailCode::getIp, ip));
        return emailCodeMapper.selectCount(emailCodeQueryWrapper);
    }

}

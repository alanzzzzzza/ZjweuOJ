package com.nextrt.acm.service.system;

import cn.hutool.core.util.RandomUtil;
import com.nextrt.core.vo.Result;
import com.nextrt.acm.biz.system.EmailBiz;
import com.nextrt.acm.config.SystemConfig;
import com.nextrt.acm.config.thread.ViThreadPoolManager;
import com.nextrt.core.entity.system.EmailCode;
import com.nextrt.acm.util.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.Map;


/**
 * Created by XYZM
 * Project: examination
 * User: xyzm
 * Date: 2020/2/28
 * info:
 */
@Service
public class EmailService {
    private final EmailBiz emailBiz;
    private final SystemConfig config;
    private final EmailUtils emailUtils;
    private final static String forgetPassUrl = "/auth/reset-password?email={email}&code={code}";

    public EmailService(EmailBiz emailBiz, SystemConfig config, EmailUtils emailUtils) {
        this.emailBiz = emailBiz;
        this.config = config;
        this.emailUtils = emailUtils;
    }

    public void send(String email, String title, String text) {
        ViThreadPoolManager.getInstance().execute(() -> emailUtils.EmailSend(email, title, text));
    }

    public void sendFile(String email, String title, String text, Map<String,String> paths) {
        ViThreadPoolManager.getInstance().execute(() -> {
            try {
                emailUtils.attachedSend(email, title, text,paths);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean checkEmailCode(String email, String code, String ip) {
        return emailBiz.getEmailCodeByEmail(email, code, ip) != null;
    }

    public Result sendRegisterEmailCode(String email, String ip) {
        if (emailBiz.countEmailCode(email, ip) > 3)
            return new Result(-3, "此IP:" + ip + ",在十分钟内请求验证码次数过于频繁！请稍后再试！");
        EmailCode emailCode = new EmailCode();
        emailCode.setIp(ip);
        emailCode.setEmail(email);
        emailCode.setCode(RandomUtil.randomNumbers(6));
        emailCode.setExpireTime(new Date(System.currentTimeMillis() + config.getInt("EmailCodeValidTime")*1000*60));
        if (emailBiz.insertEmailCode(emailCode) > 0) {
            String emailText = config.getString("EmailCodeHtml").replace("$code", emailCode.getCode());
            emailUtils.EmailSend(email, "注册验证码", emailText);
            return new Result(1, "验证码发送成功，请注意查收！");
        }
        return new Result(0, "验证码发送失败，系统出错，请联系管理员");
    }

    public Result sendFindPassEmailCode(String email, String ip, String username) {
        if (emailBiz.countEmailCode(email, ip) > 3)
            return new Result(-3, "此IP:" + ip + ",在十分钟内请求验证码次数过于频繁！请稍后再试！");
        EmailCode emailCode = new EmailCode();
        emailCode.setIp(ip);
        emailCode.setEmail(email);
        emailCode.setCode(RandomUtil.randomNumbers(6));
        emailCode.setExpireTime(new Date(System.currentTimeMillis() + config.getInt("EmailCodeValidTime")*1000*60));
        if (emailBiz.insertEmailCode(emailCode) > 0) {
            String url = (config.getString("FrontUrl") + forgetPassUrl).replace("{email}", email).replace("{code}", emailCode.getCode());
            String text = config.getString("EmailFindPassHtml").replace("$url", url).replace("$username", username);
            send(email, "找回密码", text);
            return new Result(1, "重置密码邮件发送成功，请到您所填写的邮箱中查收！");
        }
        return new Result(0, "重置密码邮件发送失败，系统出错，请联系管理员");
    }
}

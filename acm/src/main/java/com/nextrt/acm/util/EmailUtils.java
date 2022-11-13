package com.nextrt.acm.util;

import com.nextrt.acm.config.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Component
public class EmailUtils {
    private JavaMailSenderImpl mailSender;
    private final SystemConfig config;

    public EmailUtils(SystemConfig config) {
        this.config = config;
    }

    public int EmailSend(String Email, String title, String content) {
        setInitData();
        int res = 1;
        //消息处理类
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //helper.setFrom(sender);
            helper.setTo(Email);
            helper.setSubject(title);
            //设置邮件内容 ，true 表示发送html 格式
            helper.setText(content, true);
            helper.setFrom(config.getString("EmailUser"),config.getString("EmailNick"));
        } catch (MessagingException e) {
            res = -1;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mailSender.send(message);
        return res;
    }

    public void setInitData(){
        //创建邮件发送服务器
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(config.getString("EmailHost"));
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setPort(config.getInt("EmailPort"));
        mailSender.setUsername(config.getString("EmailUser"));
        mailSender.setPassword(config.getString("EmailPass"));
        //加认证机制
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", true);
        javaMailProperties.put("mail.smtp.starttls.enable", true);
        javaMailProperties.put("mail.smtp.timeout", 30000);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
    /**
     * 发送普通文本
     * @param email 对方邮箱地址
     * @param subject 主题
     * @param text 邮件内容
     */
    public void simpleMailSend(String email,String subject,String text) {
        //创建邮件内容
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(Objects.requireNonNull(mailSender.getUsername()));
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        //发送邮件
        mailSender.send(message);
    }

    /**
     * 发送附件,支持多附件
     * //使用JavaMail的MimeMessage，支付更加复杂的邮件格式和内容
     //MimeMessages为复杂邮件模板，支持文本、附件、html、图片等。
     * @param email 对方邮箱
     * @param subject 主题
     * @param text 内容
     * @param paths 附件路径，和文件名
     * @throws MessagingException
     */
    public void attachedSend(String email, String subject, String text, Map<String,String> paths) throws MessagingException {
        setInitData();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(Objects.requireNonNull(mailSender.getUsername()));
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(text);
        if (paths!=null){
            paths.forEach((k,v)->{
                FileSystemResource file = new FileSystemResource(v);
                try {
                    helper.addAttachment(k, file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        mailSender.send(message);
    }
}

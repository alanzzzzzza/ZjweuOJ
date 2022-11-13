package com.nextrt.acm.service;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.nextrt.acm.service.system.EmailService;
import com.nextrt.core.entity.user.UserLog;
import com.nextrt.core.vo.Result;
import com.nextrt.acm.biz.user.UserBiz;
import com.nextrt.acm.biz.user.UserLogBiz;
import com.nextrt.core.entity.user.User;
import com.nextrt.acm.util.PasswordEncrypt;
import com.nextrt.acm.util.RedisUtil;
import com.nextrt.acm.util.img.AvatarHelper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserBiz userBiz;
    private final RedisUtil redisUtil;
    private final UserLogBiz userLogBiz;
    private final PasswordEncrypt encrypt;
    private final EmailService emailService;

    public UserService(UserBiz userBiz, RedisUtil redisUtil, UserLogBiz userLogBiz, PasswordEncrypt encrypt, EmailService emailService) {
        this.userBiz = userBiz;
        this.redisUtil = redisUtil;
        this.userLogBiz = userLogBiz;
        this.encrypt = encrypt;
        this.emailService = emailService;
    }

    //0 成功 -1用户已经存在 -2 系统出错RegisterIPLimit
    public Result registerUser(User user, String code) {
        if (userBiz.getUserByLogin(user.getEmail(), user.getUsername()) != null)
            return new Result(-2, "该用户名已被注册!");
        if (!emailService.checkEmailCode(user.getEmail(), code, user.getRegIp()))
            return new Result(-3, "邮箱验证码错误,请检查后再试！");
        if (userBiz.detectingUsers(user)) {
            redisUtil.getLock(user.getEmail());
            user.setRegTime(new Date());//保存注册日期
            user.setPassword(encrypt.encodePassword(user.getPassword()));//对密码加密保存
            user.setAvatarUrl(AvatarHelper.createBase64Avatar(Math.abs(user.getEmail().hashCode())));
            if (userBiz.userRegister(user) == 1) {
                redisUtil.removeLock(user.getEmail());
                return new Result(1, "用户注册成功！请登录！", "");
            } else {
                redisUtil.removeLock(user.getEmail());
                return new Result(-2, "系统出错，请联系管理员！", "");
            }
        }
        return new Result(-1, "用户已存在，请登录！", "");

    }

    public Result registerUser(String email, String ip) {
        if (userBiz.getUserByLogin(email, null) != null) return new Result(-2, "该邮箱已被注册!");
        return emailService.sendRegisterEmailCode(email, ip);
    }

    public Result findPass(String email, String ip) {
        User user = userBiz.getUserByEmail(email);
        if (user == null) return new Result(-2, "该用户未注册!");
        return emailService.sendFindPassEmailCode(email, ip, user.getUsername());
    }

    public Result findPass(User user, String code, String ip) {
        if (emailService.checkEmailCode(user.getEmail(), code, ip))
            return new Result(-3, "邮箱验证码错误,请检查后再试！");
        User oldUser = userBiz.getUserByEmail(user.getEmail());
        if (oldUser == null)
            return new Result(-2, "抱歉该用户不存在！");
        oldUser.setPassword(encrypt.encodePassword(user.getPassword()));
        if (userBiz.updateUserById(oldUser) > 0) {
            userLogBiz.addLog(oldUser.getUserId(), 3, ip);//记录登陆IP
            return new Result(1, "密码重置成功，欢迎使用！");
        }
        return new Result(0, "密码重置出错，请联系管理员");
    }

    public Result loginUser(User loginUser) {
        User exitUser = userBiz.getUserByLogin(loginUser.getEmail(), loginUser.getUsername());//查找系统中是否存在该用户
        if (exitUser == null)
            return new Result(-2, "用户不存在，请检查输入，或者注册新用户！");
        if (encrypt.passwordEquals(exitUser.getPassword(), loginUser.getPassword())) {
            userLogBiz.addLog(exitUser.getUserId(), 0, loginUser.getLoginIp());//记录登陆IP
            Map<String, Object> data = new HashMap<>();
            data.put("token", encrypt.getGToken(exitUser));
            data.put("userInfo", exitUser);
            return new Result(1, "用户登陆成功，欢迎使用！", data);
        }
        userLogBiz.addLog(exitUser.getUserId(), 1, loginUser.getLoginIp());//记录错误IP
        return new Result(-1, "密码错误，请检查输入！", "");
    }

    public User userInfo(int userId) {
        return userBiz.getUserById(userId);
    }

    public Result updateUser(User user) {
        User oldUser = userBiz.getUserById(user.getUserId());
        if (oldUser == null)
            return new Result(-1, "该用户不存在，请检查请求！");
        oldUser.setNick(user.getNick());
        oldUser.setOtherInfo(user.getOtherInfo());
        oldUser.setWebsite(user.getWebsite());
        oldUser.setSchool(user.getSchool());
        oldUser.setQq(user.getQq());
        userLogBiz.addLog(user.getUserId(), 4, user.getLoginIp());//记录登陆IP
        if (userBiz.updateUserById(oldUser) == 1)
            return new Result(1, "用户信息更新成功");
        else
            return new Result(0, "用户信息更新失败");
    }

    public Result userRank(int page, int size) {
        return new Result(1, "获取成功！", userBiz.getUserRankVO(page, size));
    }

    public List<UserLog> getUserLogInfoList(int userId) {
        return userLogBiz.getUserLogInfo(userId);
    }

    public Result adminDeleteUser(int userId) {
        if (userBiz.adminDeleteUser(userId) == 1)
            return new Result(1, "用户删除成功！");
        return new Result(-1, "用户删除失败!");
    }

    public Result adminUpdateUser(User user) {
        User oldUser = userBiz.getUserById(user.getUserId());
        if (oldUser == null) return new Result(-1, "该用户不存在，请检查请求！");
        oldUser.setLevel(user.getLevel());
        if (!StrUtil.hasBlank(user.getPassword()))
            oldUser.setPassword(encrypt.encodePassword(user.getPassword()));
        if(!user.getEmail().equals(oldUser.getEmail()) &&  userBiz.getUserByEmail(user.getEmail()) != null){
            return new Result(-1,"该邮箱已被他人使用");
        }
        if (!Validator.isEmail(user.getEmail())){
            return new Result(-1,"邮箱格式错误，请检查后再试");
        }
        oldUser.setEmail(user.getEmail());
        oldUser.setSchool(user.getSchool());
        if (userBiz.updateUserById(oldUser) == 1)
            return new Result(1, "用户信息更新成功");
        else
            return new Result(0, "用户信息更新失败");
    }

    public Result adminGetUserList(int page, int size, String query) {
        return new Result(1, "获取成功", userBiz.adminGetUserList(page, size, query));
    }
}

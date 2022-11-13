package com.nextrt.acm.biz.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nextrt.acm.config.thread.ViThreadPoolManager;
import com.nextrt.acm.util.NetUtil;
import com.nextrt.core.entity.user.UserLog;
import com.nextrt.core.mapper.user.UserLogMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class UserLogBiz {
    private final UserLogMapper userLogMapper;

    public UserLogBiz(UserLogMapper userLogMapper) {
        this.userLogMapper = userLogMapper;
    }

    //添加日志记录
    @Async
    public void addLog(int userId, int type, String value) {
        ViThreadPoolManager.getInstance().execute(() -> {
            UserLog userLog = new UserLog();
            userLog.setLogTime(new Date());
            userLog.setUserId(userId);
            userLog.setLogType(type);
            userLog.setLogIp(value);
            userLog.setLogIpInfo(NetUtil.getIPInfo(value));
            userLogMapper.insert(userLog);
        });
    }

    public List<UserLog> getUserLogInfo(int userId) {
        QueryWrapper<UserLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserLog::getUserId, userId).orderByDesc(UserLog::getLogId);
        queryWrapper.last("limit 10");
        return userLogMapper.selectList(queryWrapper);
    }
}

package com.nextrt.core.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_log")
public class UserLog {
    @TableId(type = IdType.AUTO)
    private Integer logId;
    private Integer logType;//日志类型 0登陆成功 -1密码错误 1修改密码
    private Integer userId;
    private String logIp;
    private String logIpInfo;
    private Date logTime;
    @TableField(exist = false)
    private String logTypeInfo;

    public String getLogTypeInfo() {
        switch (logType) {
            case 0:
                return "登录成功";
            case 1:
                return "密码错误";
            case 2:
                return "申请改密";
            case 3:
                return "完成改密";
            case 4:
                return "更新资料";
            default:
                return "未知";
        }
    }
}

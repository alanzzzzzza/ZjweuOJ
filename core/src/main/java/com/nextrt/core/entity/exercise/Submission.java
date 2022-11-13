package com.nextrt.core.entity.exercise;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Submission {
    @TableId(type = IdType.AUTO)
    private int id;
    private int userId;
    private int problemId;
    private int contestId;
    private String code;
    private Date subTime;
    private Date judgeTime;
    private long memoryUse;
    private long timeUse;
    private int status = -1;
    @TableField(exist = false)
    private String statusInfo;
    private String otherInfo;
    private String publicIp;
    private String localIp;//加入本地IP
    private String language;
    private Long judgeUseTime = 0L;
    private int judgeId;
    private int review = 0;
    private int gotScore;
    private int score;
    private int type;//竞赛模式OI-1还是ACM-0

    public String getStatusInfo() {
        switch (this.status){
            case 0:
                return "测试通过";
            case 1:
                return "格式错误";
            case 2:
                return "时间超时";
            case 3:
                return "内存超出";
            case 4:
                return "错误答案";
            case 5:
                return "运行错误";
            case 6:
                return "输出超限";
            case 7:
                return "编译出错";
            case 8:
                return "系统出错";
            case 9:
                return "安全问题";
            case 10:
                return "部分正确";
            default:
                return "等待判题";
        }
    }
}

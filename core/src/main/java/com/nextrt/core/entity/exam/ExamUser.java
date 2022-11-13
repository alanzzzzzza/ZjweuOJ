package com.nextrt.core.entity.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("exam_user")
public class ExamUser {
    @TableId(type = IdType.AUTO)
    private Integer contestUserId;
    private Integer contestId;//竞赛ID
    private String username;//用户名
    private String schoolNo;//学号
    private String name;//姓名
    private String school;//学校
    private String team;//队伍名称
    private String className;//班级
    private String password;//密码
    private Integer status = 0;//状态 0正常 1冻结
    private Date addTime;//添加时间
    private Integer sub;//Sub Num
    private Integer score;//分数
    private Integer ac;//Accept Num
    private Date joinTime;//加入时间
    private Date startTime;//开始答题时间
    private String startLocalIp;//加入本地IP
    private String startPublicIp;//加入公网IP
    private String joinLocalIp;//加入本地IP
    private String joinPublicIp;//加入公网IP
    private int suspicious = 0;//是否可疑
    private Date endTime;//作答结束时间
}

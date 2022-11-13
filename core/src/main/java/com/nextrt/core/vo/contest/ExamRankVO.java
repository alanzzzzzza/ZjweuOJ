package com.nextrt.core.vo.contest;

import lombok.Data;

import java.util.Date;
@Data
public class ExamRankVO {
    private Integer contestUserId;
    private String username;//用户名
    private String schoolNo;//学号
    private String name;//姓名
    private String school;//学校
    private String team;//队伍名称
    private String className;//班级
    private Integer sub;//Sub Num
    private Integer score;//分数
    private Integer ac;//Accept Num
    private Date endSubTime;//最后提交时间
}

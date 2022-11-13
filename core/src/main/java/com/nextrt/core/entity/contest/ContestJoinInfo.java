package com.nextrt.core.entity.contest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 2020/2/27
 * info: 加入竞赛
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("contest_join_infos")
public class ContestJoinInfo {
    @TableId(type = IdType.AUTO)
    private int contestJoinId;
    private Integer contestId;//竞赛ID
    private int userId;//用户ID
    private Date joinTime;//加入时间
    private Date startTime;//开始答题时间
    private Integer sub;//Sub Num
    private Integer score;//分数
    private Integer ac;//Accept Num
    private int suspicious = 0;//是否可疑
    private Date endTime;//作答结束时间
    private String startLocalIp;//加入本地IP
    private String startPublicIp;//加入公网IP
    private String joinLocalIp;//加入本地IP
    private String joinPublicIp;//加入公网IP
    @TableField(exist = false)
    private String roomId;
    @TableField(exist = false)
    private String password;
}

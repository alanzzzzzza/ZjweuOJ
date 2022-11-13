package com.nextrt.core.vo.user;

import lombok.Data;

import java.util.Date;

/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 20-3-4
 * info:
 */
@Data
public class UserJoinContest {
    private int contestId;//竞赛ID
    private int type;//竞赛ID
    private Date joinTime;//加入时间
    private String name;//用户加入竞赛信息
    private Date startTime;//用户加入竞赛信息
    private Date endTime;//用户加入竞赛信息
}

package com.nextrt.core.vo.user;

import lombok.Data;

/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 20-3-2
 * info: 用户排行版
 */
@Data
public class UserRankVO {
    private int userId;
    private String username;
    private String nick;
    private Integer acNum = 0;//通过数
    private Integer subNum = 0;//提交数
    private Integer acpNum = 0;//通过题目数
    private Integer subpNum = 0;//提交题目数
}

package com.nextrt.core.vo.contest;

import lombok.Data;

/**
 * @Author: 轩辕子墨
 * @Project: nextoj
 * @Date: 2020/3/2
 * @Description:
 */
@Data
public class ContestRankVO {
    private String nick;
    private String username;
    private Integer userId;
    private Integer sub;//Sub Num
    private Integer score;//分数
    private Integer ac;//Accept Num
}

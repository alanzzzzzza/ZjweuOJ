package com.nextrt.core.vo;

import com.nextrt.core.entity.contest.Contest;
import com.nextrt.core.entity.contest.ContestJoinInfo;
import lombok.Data;

/**
 * @Author: 轩辕子墨
 * @Project: nextoj
 * @Date: 2020/3/1
 * @Description:
 */
@Data
public class ContestInfo {
    private Contest contest;
    private ContestJoinInfo contestJoinInfo;
}

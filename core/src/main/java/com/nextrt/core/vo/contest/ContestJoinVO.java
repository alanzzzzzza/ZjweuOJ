package com.nextrt.core.vo.contest;

import com.nextrt.core.entity.contest.ContestJoinInfo;
import lombok.Data;

@Data
public class ContestJoinVO extends ContestJoinInfo {
    private String username;
    private String nick;
    private String email;
}

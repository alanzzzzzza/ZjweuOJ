package com.nextrt.core.vo.contest;

import com.nextrt.core.entity.contest.Contest;
import com.nextrt.core.entity.exam.ExamUser;
import lombok.Data;

@Data
public class ExamContestInfo {
    private Contest contest;
    private ExamUser examUser;
}

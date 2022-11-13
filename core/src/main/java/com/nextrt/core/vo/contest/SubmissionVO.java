package com.nextrt.core.vo.contest;

import com.nextrt.core.entity.exercise.Submission;
import lombok.Data;

/**
 * @Author: 轩辕子墨
 * @Project: nextoj
 * @Date: 2020/3/1
 * @Description:
 */
@Data
public class SubmissionVO extends Submission {
    private String username;
    private String nick;
    private String schoolNo;
    private String team;
    private String title;
}

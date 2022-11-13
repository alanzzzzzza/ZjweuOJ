package com.nextrt.core.vo.contest;

import com.nextrt.core.entity.exercise.Problem;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 轩辕子墨
 * @Project: nextoj
 * @Date: 2020/3/1
 * @Description:
 */
@Data
@NoArgsConstructor
public class ProblemVO extends Problem {
    private int subStatus;//解决状态
    private int submissionNum = 0;//提交数
    private int acceptNum = 0;//提供数
}

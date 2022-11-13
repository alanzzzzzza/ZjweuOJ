package com.nextrt.core.entity.contest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 2020/2/27
 * info: 竞赛题目实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("contest_problems")
public class ContestProblem {
    @TableId(type = IdType.AUTO)
    private int contestProblemId;
    private int contestId;
    private int problemId;
    private int submissionNum = 0;//提交数
    private int acceptNum = 0;//提供数
}

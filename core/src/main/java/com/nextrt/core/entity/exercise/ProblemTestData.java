package com.nextrt.core.entity.exercise;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Program: nextoj
 * @Description: 问题测试数据类
 * @Author: 轩辕子墨
 * @Create: 2019-12-11 14:52
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("problem_test_data")
@Builder
public class ProblemTestData {
    @TableId(type = IdType.AUTO)
    private Integer problemTestId;
    private String output;
    private String input;
    private Integer problemId;
    private Integer score = 0;
}

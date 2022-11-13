package com.nextrt.core.entity.contest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: 轩辕子墨
 * @Project: nextoj
 * @Date: 2020/3/1
 * @Description:
 */
@Data
@NoArgsConstructor
@TableName("contest_problem_results")
public class ContestProblemResult {
    @TableId(type = IdType.AUTO)
    private int resultId;
    private int userId;//用户ID
    private int contestId;//竞赛ID
    private int problemId;//问题ID
    private String problemName;
    private int status;//状态
    @TableField(exist = false)
    private String statusInfo;
    private int subNum;//提交次数
    private int score;//分数
    private Date acTime;//首次通过时间
    public String getStatusInfo() {
        switch (this.status){
            case 0:
                return "测试通过";
            case 1:
                return "格式错误";
            case 2:
                return "时间超时";
            case 3:
                return "内存超出";
            case 4:
                return "错误答案";
            case 5:
                return "运行错误";
            case 6:
                return "输出超限";
            case 7:
                return "编译出错";
            case 8:
                return "系统出错";
            case 9:
                return "安全问题";
            default:
                return "等待判题";
        }
    }
}

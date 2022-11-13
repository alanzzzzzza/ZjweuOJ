package com.nextrt.core.entity.exercise;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Program: nextoj
 * @Description: 问题变更日志
 * @Author: 轩辕子墨
 * @Create: 2019-12-26 17:48
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("problem_operation_log")
public class ProblemOperationLog {
    @TableId(type = IdType.AUTO)
    private Integer operationId;//操作日志编号
    private Integer userId = 0;//操作用户
    private String operationType;//操作类型
    private Date operationTime;//操作时间
    private String operationIp;//用户操作IP
    private String otherInfo;//执行结果
}

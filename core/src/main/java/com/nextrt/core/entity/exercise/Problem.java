package com.nextrt.core.entity.exercise;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("problem")
public class Problem {
    @TableId(type = IdType.AUTO)
    private Integer id;//问题编号
    private Integer userId = 0;//属于用户 0为系统添加
    @NotBlank(message = "问题标题不能为空！")
    private String title;//问题名字
    @NotBlank(message = "问题内容不能为空！")
    private String content;//问题内容
    private String inputDescription;//输入描述
    private String outputDescription;//输出描述
    private String inputExample;//样例输入
    @NotBlank(message = "输出样例不能为空！")
    private String outputExample;//样例输出
    private String hint;//提示
    @NotBlank(message = "问题来源不能为空！")
    private String source;//问题的来源
    @Min(value = 1,message = "内存限制不能为空!")
    private Long memoryLimit;//时间限制
    @Min(value = 1,message = "时间限制不能为空!")
    private Long timeLimit;//内存限制
    private Integer difficulty = 0;//难度评级
    private Integer submissionNumber = 0;//提交数
    private Integer acceptedNumber = 0;//通过数
    private int isPublic = 1;//问题是否公开 0公开 1不公开
    private Date addTime;//题目添加时间
    private Date updateTime;//上次更新时间
    private int status;//0 下架状态 1正常状态
    @Version
    private Integer version = 0;//版本号，用于乐观锁
}

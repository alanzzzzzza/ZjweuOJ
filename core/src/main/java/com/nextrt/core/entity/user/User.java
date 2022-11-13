package com.nextrt.core.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    @Email(message = "邮箱填写有误！")
    private String email;//邮箱
    @NotBlank(message = "用户名不能为空！")
    private String username;//用户名 学号
    private Integer status = 0;//状态 0正常 1冻结
    private String regIp;//注册Ip
    private Date regTime;//注册时间
    private String language = "zh-cn";//用户语言
    private String password;//用户密码
    @NotBlank(message = "昵称不能为空！")
    private String nick;//昵称 显示在Web上
    private String avatarUrl;//头像地址
    private String school;//学校信息
    private String qq;//QQ
    private String website;//个人首页
    private String otherInfo;//其他信息，非本校学生
    private Long phone;//手机号
    private Integer acNum = 0;//通过数
    private Integer subNum = 0;//提交数
    private Integer acpNum = 0;//通过题目数
    private Integer subpNum = 0;//提交题目数
    private Integer level = 1;//1 普通用户 3系统管理员
    @JsonIgnore
    @TableField(exist = false)
    private String loginIp;
    @Version
    @JsonIgnore
    private Integer version;//乐观锁
}
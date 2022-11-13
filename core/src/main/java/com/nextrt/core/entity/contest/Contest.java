package com.nextrt.core.entity.contest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("contests")
public class Contest {
    @TableId(type = IdType.AUTO)
    private int contestId;//竞赛ID
    @NotBlank(message = "竞赛名称不能为空")
    private String name;//竞赛名称
    @NotBlank(message = "竞赛说明不能为空")
    private String description;//竞赛说明
    private Date showTime;//开始时间
    @Future(message = "开始时间不能早于当前时间")
    private Date startTime;//开始时间
    @Future(message = "结束时间不能早于当前时间")
    private Date endTime;//结束时间
    @Max(value = 5,message = "竞赛时间太短")
    private String roomId;
    private long totalTime;//总时间 分钟
    private int isPublic;//是否公共可见0 不可见 1可见
    private int isTestAccount = 0;//是否为测试帐号登陆0不是 1是
    private int status = 1;//0未发布 1已发布
    private int type;//竞赛模式OI-1还是ACM-0
    private String password;//访问密码
    private String publicIpRemark;//公网IP限制
    private int endVisible = 1;//竞赛结束后用户是否可见竞赛信息 0 不可见 1可见
    private Date addTime;//添加时间
    private Integer userId;//添加用户
    private String language = "Java,C,C++,Go,Python2,Python3,Ruby,C#";
    @TableField(exist = false)
    private List<Integer> problemIds;
}

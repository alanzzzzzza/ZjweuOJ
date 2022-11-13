package com.nextrt.core.entity.contest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("contest_ip_reports")
public class ContestIPReport {
    @TableId(type = IdType.AUTO)
    private int contestIpReportId;//编号
    private int userId;//用户ID
    private Integer contestId;//竞赛ID
    private Date reportTime;//报告时间
    @NotBlank(message = "本机IP不能为空")
    private String localIp;//本地IP
    private String publicIp;//外网IP
}

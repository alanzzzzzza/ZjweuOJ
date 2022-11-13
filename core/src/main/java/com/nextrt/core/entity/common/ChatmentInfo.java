package com.nextrt.core.entity.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("chatment_info")
public class ChatmentInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;//公告编号
    private Integer pid;//公告编号
    private Integer userId = 0;//添加用户
    private Date addTime = new Date();//公告添加时间
    private String content;//公告内容
}

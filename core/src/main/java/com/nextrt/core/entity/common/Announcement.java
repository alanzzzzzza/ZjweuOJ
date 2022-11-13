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
@TableName("announcements")
public class Announcement {
    @TableId(type = IdType.AUTO)
    private Integer id;//公告编号
    private Integer userId = 0;//添加用户
    private Integer showType = 0;//显示类型
    private String title;//公告标题
    private Date addTime = new Date();//公告添加时间
    private Date showTime;//公告开始展示时间
    private Date expireTime;//公告过期时间
    private String content;//公告内容
}

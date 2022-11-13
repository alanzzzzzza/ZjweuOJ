package com.nextrt.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 2020/2/17
 * info:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("email_code")
public class EmailCode {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer status = 0;
    private String email;
    private String code;
    private Date expireTime;
    private String ip;
    private String useIp;
    @Version
    @JsonIgnore
    private Integer version = 0;//乐观锁
}

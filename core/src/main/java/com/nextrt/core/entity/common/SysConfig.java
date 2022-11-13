package com.nextrt.core.entity.common;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 19-12-31
 * info:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_config")
public class SysConfig {
    @TableId
    private String name;
    private String value;
    private String nick;
    private Integer type;
}

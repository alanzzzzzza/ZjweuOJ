package com.nextrt.core.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Program: nextoj
 * @Description: 系统消息传递
 * @Author: 轩辕子墨
 * @Create: 2020-01-01 11:40
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMsg {
    private int status;
    private Object data;
    private String desc;

    public SysMsg(int status, Object data) {
        this.status = status;
        this.data = data;
    }
}

package com.nextrt.core.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    int status = 0;
    String msg;
    Object data;
    long timestamp = System.currentTimeMillis();

    public Result(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Result(int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
}

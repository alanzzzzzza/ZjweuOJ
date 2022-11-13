package com.nextrt.core.vo.judge;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 2020/2/23
 * info:
 */
@Data
@AllArgsConstructor
public class HeartResult {
    private Object data;
    private String error;

    public HeartResult(Object data) {
        this.data = data;
    }
}

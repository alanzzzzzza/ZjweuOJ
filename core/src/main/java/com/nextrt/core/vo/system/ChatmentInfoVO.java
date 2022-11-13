package com.nextrt.core.vo.system;

import com.nextrt.core.entity.common.Chatment;
import com.nextrt.core.entity.common.ChatmentInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 20-3-3
 * info: 公告展示对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatmentInfoVO extends ChatmentInfo {
    private String username;
}

package com.nextrt.core.vo.system;

import com.nextrt.core.entity.common.Announcement;
import com.nextrt.core.entity.common.Chatment;
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
public class ChatmentVO extends Chatment {
    private String username;
}

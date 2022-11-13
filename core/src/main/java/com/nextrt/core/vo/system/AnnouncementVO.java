package com.nextrt.core.vo.system;

import com.nextrt.core.entity.common.Announcement;
import lombok.Data;

/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 20-3-3
 * info: 公告展示对象
 */
@Data
public class AnnouncementVO extends Announcement {
    private String nick;
}

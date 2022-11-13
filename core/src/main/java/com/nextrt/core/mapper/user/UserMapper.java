package com.nextrt.core.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.user.User;
import com.nextrt.core.vo.user.UserRankVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where status = 0 order by acp_num desc")
    IPage<UserRankVO> getUserRank(Page<?> page);
}

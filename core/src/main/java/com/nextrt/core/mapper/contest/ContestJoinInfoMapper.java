package com.nextrt.core.mapper.contest;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.contest.ContestJoinInfo;
import com.nextrt.core.vo.contest.ContestJoinVO;
import com.nextrt.core.vo.contest.ContestRankVO;
import com.nextrt.core.vo.user.UserJoinContest;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 2020/2/27
 * info:
 */
@Repository
public interface ContestJoinInfoMapper extends BaseMapper<ContestJoinInfo> {

    @Select("select cji.*,u.username,u.nick,u.email from contest_join_infos as cji,user as u where cji.contest_id = #{contestId} and cji.user_id = u.user_id")
    List<ContestJoinVO> getContestJoinVOByCid(int contestId);

    @Select("select cji.*,u.username,u.nick,u.email from contest_join_infos as cji,user as u where cji.contest_id = #{contestId} and cji.user_id = u.user_id")
    IPage<ContestJoinVO> getContestJoinVO(Page<?> page,int contestId);

    @Select("select c.name,c.contest_id,c.type,cji.join_time,cji.start_time,cji.end_time from contest_join_infos as cji,contests as c where cji.contest_id = c.contest_id and cji.user_id =#{userId} order by cji.contest_join_id desc")
    IPage<UserJoinContest> getUserJoinContest(Page<?> page,int userId);


    @Select("select cji.ac , cji.sub , cji.score , u.nick, u.username, u.user_id from user as u, contest_join_infos as cji where cji.contest_id = #{contestId} and u.user_id = cji.user_id order by cji.ac desc, cji.sub ")
    List<ContestRankVO> getContestRankByAcm(int contestId);

    @Select("select cji.ac , cji.sub , cji.score , u.nick, u.username, u.user_id from user as u, contest_join_infos as cji where cji.contest_id = #{contestId} and u.user_id = cji.user_id order by cji.score desc, cji.ac desc, cji.sub ")
    List<ContestRankVO> getContestRankByOi(int contestId);

}

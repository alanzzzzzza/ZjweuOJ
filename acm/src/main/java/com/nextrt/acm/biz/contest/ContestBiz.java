package com.nextrt.acm.biz.contest;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.contest.Contest;
import com.nextrt.core.mapper.contest.ContestMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ContestBiz {
    private final ContestMapper contestMapper;

    public ContestBiz(ContestMapper contestMapper) {
        this.contestMapper = contestMapper;
    }

    public synchronized int addContest(Contest contest) {
        contest.setAddTime(new Date());
        return contestMapper.insert(contest);
    }

    public int updateContest(Contest contest) {
        return contestMapper.updateById(contest);
    }

    public int deleteContest(int id) {
        return contestMapper.deleteById(id);
    }

    //管理员获取竞赛列表
    public IPage<Contest> adminGetContestListByPage(String name, int page, int size) {
        Page<Contest> aPage = new Page<Contest>(page, size);
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.hasBlank(name)) queryWrapper.lambda().like(Contest::getName, name);
        queryWrapper.lambda().select(Contest::getContestId,Contest::getType,Contest::getStatus,Contest::getRoomId,Contest::getPassword,Contest::getEndVisible,Contest::getIsTestAccount, Contest::getType, Contest::getName, Contest::getStartTime, Contest::getEndTime, Contest::getIsPublic, Contest::getTotalTime).orderByDesc(Contest::getContestId);
        return contestMapper.selectPage(aPage, queryWrapper);
    }


    //管理员获取竞赛列表
    public IPage<Contest> teacherGetContestListByPage(String name,int userId, int page, int size) {
        Page<Contest> aPage = new Page<Contest>(page, size);
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Contest::getUserId,userId);
        if (!StrUtil.hasBlank(name)) queryWrapper.lambda().like(Contest::getName, name);
        queryWrapper.lambda().select(Contest::getContestId,Contest::getType,Contest::getStatus,Contest::getRoomId,Contest::getPassword,Contest::getEndVisible,Contest::getIsTestAccount, Contest::getType, Contest::getName, Contest::getStartTime, Contest::getEndTime, Contest::getIsPublic, Contest::getTotalTime).orderByDesc(Contest::getContestId);
        return contestMapper.selectPage(aPage, queryWrapper);
    }


    //管理员获取竞赛信息
    public Contest getContestById(int contestId) {
        return contestMapper.selectById(contestId);
    }

    public Contest getContestByRoomId(String roomId) {
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Contest::getRoomId, roomId);
        return contestMapper.selectOne(queryWrapper);
    }

    //用户获取公开竞赛列表
    public IPage<Contest> userGetContestListByPage(String name, int page, int size) {
        Page<Contest> aPage = new Page<Contest>(page, size);
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.hasBlank(name)) queryWrapper.lambda().like(Contest::getName, name);
        queryWrapper.lambda().eq(Contest::getIsPublic, 1)
                .eq(Contest::getStatus, 1)
                .eq(Contest::getIsTestAccount, 0)
                .le(Contest::getShowTime, new Date())
                .select(Contest::getContestId, Contest::getEndVisible, Contest::getType, Contest::getDescription, Contest::getName, Contest::getStartTime, Contest::getEndTime, Contest::getTotalTime)
                .orderByDesc(Contest::getContestId);
        return contestMapper.selectPage(aPage, queryWrapper);
    }


    public Contest userGetContestInfo(int contestId) {
        QueryWrapper<Contest> contestQueryWrapper = new QueryWrapper<>();
        contestQueryWrapper.lambda().eq(Contest::getContestId, contestId)
                .eq(Contest::getStatus, 1)
                .eq(Contest::getIsTestAccount, 0)
                .le(Contest::getShowTime, new Date())
                .select(Contest::getRoomId, Contest::getEndVisible,Contest::getLanguage, Contest::getContestId, Contest::getType, Contest::getName, Contest::getDescription, Contest::getStartTime, Contest::getEndTime, Contest::getIsPublic, Contest::getTotalTime);
        return contestMapper.selectOne(contestQueryWrapper);
    }
    public Contest examGetContestInfo(int contestId) {
        QueryWrapper<Contest> contestQueryWrapper = new QueryWrapper<>();
        contestQueryWrapper.lambda().eq(Contest::getContestId, contestId)
                .eq(Contest::getStatus, 1)
                .eq(Contest::getIsTestAccount, 1)
                .le(Contest::getShowTime, new Date())
                .select(Contest::getRoomId, Contest::getEndVisible,Contest::getLanguage, Contest::getContestId, Contest::getType, Contest::getName, Contest::getDescription, Contest::getStartTime, Contest::getEndTime, Contest::getIsPublic, Contest::getTotalTime);
        return contestMapper.selectOne(contestQueryWrapper);
    }

}

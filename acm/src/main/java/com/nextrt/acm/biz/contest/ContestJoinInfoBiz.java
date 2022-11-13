package com.nextrt.acm.biz.contest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.contest.ContestJoinInfo;
import com.nextrt.core.mapper.contest.ContestJoinInfoMapper;
import com.nextrt.core.vo.contest.ContestJoinVO;
import com.nextrt.core.vo.contest.ContestRankVO;
import com.nextrt.core.vo.user.UserJoinContest;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ContestJoinInfoBiz {
    private final ContestJoinInfoMapper joinInfoMapper;

    public ContestJoinInfoBiz(ContestJoinInfoMapper contestJoinInfoMapper) {
        this.joinInfoMapper = contestJoinInfoMapper;
    }

    //用户加入竞赛
    public int add(ContestJoinInfo contestJoinInfo) {
        contestJoinInfo.setJoinTime(new Date());
        return joinInfoMapper.insert(contestJoinInfo);
    }

    //根据竞赛ID和用户ID获取加入信息
    public ContestJoinInfo getContestJoinInfo(int contestId, int userId) {
        QueryWrapper<ContestJoinInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestJoinInfo::getContestId, contestId).eq(ContestJoinInfo::getUserId, userId);
        return joinInfoMapper.selectOne(queryWrapper);
    }

    //更新用户竞赛加入信息
    public int updateContestJoinInfo(ContestJoinInfo contestJoinInfo) {
       return joinInfoMapper.updateById(contestJoinInfo);
    }

    //根据竞赛ID和用户ID删除加入信息
    public int deleteByContestIdAndUserId(int userId,int contestId){
        QueryWrapper<ContestJoinInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestJoinInfo::getContestId, contestId).eq(ContestJoinInfo::getUserId,userId);
        return joinInfoMapper.delete(queryWrapper);
    }

    //根据竞赛删除用户加入信息
    public void deleteByContestId(int contestId) {
        QueryWrapper<ContestJoinInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestJoinInfo::getContestId, contestId);
        joinInfoMapper.delete(queryWrapper);
    }

    public List<ContestJoinInfo> getContestJoinInfoList(int contestId) {
        QueryWrapper<ContestJoinInfo> contestJoinInfoQueryWrapper = new QueryWrapper<>();
        contestJoinInfoQueryWrapper.lambda().eq(ContestJoinInfo::getContestId, contestId);
        return joinInfoMapper.selectList(contestJoinInfoQueryWrapper);
    }

    public List<ContestJoinVO> getContestJoinVO(int contestId) {
        return joinInfoMapper.getContestJoinVOByCid(contestId);
    }

    public IPage<ContestJoinVO> getContestJoinVO(int page, int size, int contestId) {
        Page<ContestJoinVO> aPage = new Page<ContestJoinVO>(page, size);
        return joinInfoMapper.getContestJoinVO(aPage, contestId);
    }

    //用户获取加入竞赛列表
    public IPage<UserJoinContest> getUserJoinContestList(int contestId, int page, int size) {
        Page<ContestJoinVO> aPage = new Page<ContestJoinVO>(page, size);
        return joinInfoMapper.getUserJoinContest(aPage, contestId);
    }

    public List<ContestRankVO> getContestRankByAcm(int contestId) {
        return joinInfoMapper.getContestRankByAcm(contestId);
    }
    public List<ContestRankVO> getContestRankByOi(int contestId) {
        return joinInfoMapper.getContestRankByOi(contestId);
    }
}

package com.nextrt.acm.biz.contest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nextrt.core.entity.contest.ContestProblemResult;
import com.nextrt.core.mapper.contest.ContestProblemResultMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContestProblemResultBiz {
    private final ContestProblemResultMapper resultMapper;

    public ContestProblemResultBiz(ContestProblemResultMapper resultMapper) {
        this.resultMapper = resultMapper;
    }

    public int add(ContestProblemResult contestProblemResult) {
        return resultMapper.insert(contestProblemResult);
    }

    public int update(ContestProblemResult contestProblemResult) {
        return resultMapper.updateById(contestProblemResult);
    }
    public int deleteByContestId(int contestId) {
        QueryWrapper<ContestProblemResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestProblemResult::getContestId, contestId);
        return resultMapper.delete(queryWrapper);
    }
    public int deleteByContestIdAndUserId(int contestId,int userId) {
        QueryWrapper<ContestProblemResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestProblemResult::getContestId, contestId).eq(ContestProblemResult::getUserId,userId);
        return resultMapper.delete(queryWrapper);
    }
    public ContestProblemResult getContestProblemResult(int userId, int contestId, int problemId) {
        QueryWrapper<ContestProblemResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestProblemResult::getContestId, contestId).eq(ContestProblemResult::getUserId, userId).eq(ContestProblemResult::getProblemId, problemId);
        return resultMapper.selectOne(queryWrapper);
    }

    //获取用户答题结果
    public List<ContestProblemResult> getUserContestProblemResult(int userId, int contestId) {
        QueryWrapper<ContestProblemResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestProblemResult::getContestId, contestId).eq(ContestProblemResult::getUserId, userId);
        return resultMapper.selectList(queryWrapper);
    }

    //获取用户答题结果
    public List<ContestProblemResult> getExamUserContestProblemResult(int userId, int contestId) {
        QueryWrapper<ContestProblemResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestProblemResult::getContestId, contestId).eq(ContestProblemResult::getUserId, userId);
        return resultMapper.selectList(queryWrapper);
    }

    //获取竞赛所有用户答题结果
    public List<ContestProblemResult> getContestProblemResultList(int contestId) {
        QueryWrapper<ContestProblemResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestProblemResult::getContestId, contestId);
        return resultMapper.selectList(queryWrapper);
    }


}

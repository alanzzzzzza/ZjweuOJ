package com.nextrt.acm.biz.exercise;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.exercise.Problem;
import com.nextrt.core.entity.exercise.ProblemOperationLog;
import com.nextrt.core.entity.exercise.ProblemTestData;
import com.nextrt.core.mapper.exercise.ProblemMapper;
import com.nextrt.core.mapper.exercise.ProblemOperationLogMapper;
import com.nextrt.core.mapper.exercise.ProblemTestDataMapper;
import com.nextrt.core.vo.contest.ProblemVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProblemBiz {
    private final ProblemMapper problemMapper;
    private final ProblemTestDataMapper problemTestDataMapper;
    private final ProblemOperationLogMapper problemOperationLogMapper;

    public ProblemBiz(ProblemMapper problemMapper, ProblemTestDataMapper problemTestDataMapper, ProblemOperationLogMapper problemOperationLogMapper) {
        this.problemMapper = problemMapper;
        this.problemTestDataMapper = problemTestDataMapper;
        this.problemOperationLogMapper = problemOperationLogMapper;
    }

    public int addProblem(Problem problem) {
        problemMapper.insert(problem);
        return problemMapper.userGetLatestProblemId();
    }

    public int updateProblem(Problem problem) {
        return problemMapper.updateById(problem);
    }

    public int deleteProblem(int problemId) {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Problem::getId, problemId);
        return problemMapper.delete(queryWrapper);
    }

    public int deleteProblemById(int problemId) {
        return problemMapper.deleteById(problemId);
    }
    public int deleteProblem(int problemId,int userId) {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Problem::getUserId,userId).eq(Problem::getId,problemId);
        return problemMapper.delete(queryWrapper);
    }

    public Problem getAdminProblemById(int problemId) {
        return problemMapper.selectById(problemId);
    }

    public Problem getProblemById(int problemId) {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_public", 1);
        queryWrapper.lambda().eq(Problem::getId, problemId);
        return problemMapper.selectOne(queryWrapper);
    }

    public IPage<ProblemVO> userGetProblemsList(int difficulty,int userId, String name, int page, int size){
        Page<Problem> aPage = new Page<Problem>(page, size);
        return problemMapper.userGetProblemsList(aPage,userId,difficulty,name);
    }

    public IPage<Problem> getProblems(int difficulty, String name, int page, int size) {
        Page<Problem> aPage = new Page<Problem>(page, size);
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(Problem::getId, Problem::getTitle, Problem::getSource, Problem::getDifficulty, Problem::getSubmissionNumber, Problem::getAcceptedNumber);
        if (difficulty >= 0)
            queryWrapper.lambda().eq(Problem::getDifficulty, difficulty);
        if (!StrUtil.hasBlank(name))
            queryWrapper.lambda().like(Problem::getTitle, name);
        return problemMapper.selectPage(aPage, queryWrapper);
    }
    public IPage<Problem> getProblems(int userId,int difficulty, String name, int page, int size) {
        Page<Problem> aPage = new Page<Problem>(page, size);
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(Problem::getId, Problem::getTitle, Problem::getSource, Problem::getDifficulty, Problem::getSubmissionNumber, Problem::getAcceptedNumber).eq(Problem::getUserId,userId);
        if (difficulty >= 0)
            queryWrapper.lambda().eq(Problem::getDifficulty, difficulty);
        if (!StrUtil.hasBlank(name))
            queryWrapper.lambda().like(Problem::getTitle, name);
        return problemMapper.selectPage(aPage, queryWrapper);
    }

    public int addProblemTestData(ProblemTestData problemTestData) {
        return problemTestDataMapper.insert(problemTestData);
    }

    public int deleteProblemTestDataByProblemId(int problemId) {
        QueryWrapper<ProblemTestData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProblemTestData::getProblemId, problemId);
        return problemTestDataMapper.delete(queryWrapper);
    }

    public List<ProblemTestData> getProblemTestDataByProblemId(int problemId) {
        QueryWrapper<ProblemTestData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProblemTestData::getProblemId, problemId);
        return problemTestDataMapper.selectList(queryWrapper);
    }

    public ProblemTestData getProblemTestDataById(int problemTestId) {
        return problemTestDataMapper.selectById(problemTestId);
    }

    public int deleteProblemTestDataById(int testId) {
        return problemTestDataMapper.deleteById(testId);
    }

    public int updateProblemTestData(ProblemTestData problemTestData) {
        return problemTestDataMapper.updateById(problemTestData);
    }

    @Async
    public void insertOperationLog(ProblemOperationLog problemOperationLog) {
        problemOperationLogMapper.insert(problemOperationLog);
    }
}

package com.nextrt.acm.biz.contest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nextrt.core.entity.contest.ContestProblem;
import com.nextrt.core.mapper.contest.ContestProblemMapper;
import com.nextrt.core.vo.contest.ProblemExcelVO;
import com.nextrt.core.vo.contest.ProblemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContestProblemBiz {
    private final ContestProblemMapper contestProblemMapper;

    public ContestProblemBiz(ContestProblemMapper contestProblemMapper) {
        this.contestProblemMapper = contestProblemMapper;
    }

    //插入竞赛对应的问题id信息
    public void addContestProblem(List<Integer> problemIds, int contestId) {
        problemIds = problemIds.stream().distinct().collect(Collectors.toList());
        List<Integer> list = getContestProblemId(contestId).stream().map(ContestProblem::getProblemId).collect(Collectors.toList());
        problemIds.forEach(x -> {
            if (!list.contains(x)) {
                ContestProblem contestProblem = new ContestProblem();
                contestProblem.setProblemId(x);
                contestProblem.setContestId(contestId);
                contestProblemMapper.insert(contestProblem);
            }
        });
    }

    public void updateContestProblem(List<Integer> problemIds, int contestId) {
        problemIds = problemIds.stream().distinct().collect(Collectors.toList());
        List<Integer> list = getContestProblemId(contestId).stream().map(ContestProblem::getProblemId).collect(Collectors.toList());
        problemIds.forEach(x -> {
            if (list.contains(x)) {
                list.remove(x);
            } else {
                ContestProblem contestProblem = new ContestProblem();
                contestProblem.setProblemId(x);
                contestProblem.setContestId(contestId);
                contestProblemMapper.insert(contestProblem);
            }
        });
        list.forEach(x -> {
            deleteContestProblem(x, contestId);
        });
    }

    public void deleteContestProblem(int problemId, int contestId) {
        QueryWrapper<ContestProblem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestProblem::getProblemId, problemId).eq(ContestProblem::getContestId, contestId);
        contestProblemMapper.delete(queryWrapper);
    }

    public int deleteContestProblem(int contestProblemId) {
        return contestProblemMapper.deleteById(contestProblemId);
    }

    public void deleteAllContestProblem(int contestId) {
        QueryWrapper<ContestProblem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestProblem::getContestId, contestId);
        contestProblemMapper.delete(queryWrapper);
    }
    public void updateContestProblem(ContestProblem contestProblem) {
        contestProblemMapper.updateById(contestProblem);
    }

    public ContestProblem getContestProblem(int problemId, int contestId) {
        QueryWrapper<ContestProblem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestProblem::getContestId, contestId).eq(ContestProblem::getProblemId, problemId);
        return contestProblemMapper.selectOne(queryWrapper);
    }
    //获取该竞赛的问题ID列表
    public List<ContestProblem> getContestProblemId(int contestId) {
        QueryWrapper<ContestProblem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestProblem::getContestId, contestId).select(ContestProblem::getProblemId);
        return contestProblemMapper.selectList(queryWrapper);
    }

    //管理员获取问题列表
    public List<ProblemVO> adminGetContestProblemListById(int contestId) {
        return contestProblemMapper.adminGetContestProblemList(contestId);
    }

    public List<ProblemExcelVO> getContestProblemListByExcel(int contestId) {
        return contestProblemMapper.getContestProblemListByExcel(contestId);
    }

    //用户获取竞赛题目信息
    public List<ProblemVO> userGetProblemInfoByContestId(int contestId,int userId) {
        return contestProblemMapper.userGetContestProblemList(contestId,userId);
    }

    //用户获取竞赛题目信息
    public List<ProblemVO> examGetProblemInfoByContestId(int contestId,int userId) {
        return contestProblemMapper.examGetContestProblemList(contestId,userId);
    }
}

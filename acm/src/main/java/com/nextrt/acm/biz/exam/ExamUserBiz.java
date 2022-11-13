package com.nextrt.acm.biz.exam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.exam.ExamUser;
import com.nextrt.core.mapper.exam.ExamUserMapper;
import com.nextrt.core.vo.contest.ExamRankVO;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ExamUserBiz {
    private final ExamUserMapper examUserMapper;

    public ExamUserBiz(ExamUserMapper examUserMapper) {
        this.examUserMapper = examUserMapper;
    }

    public int add(ExamUser examUser) {
        examUser.setAddTime(new Date());
        return examUserMapper.insert(examUser);
    }

    public int update(ExamUser examUser) {
        return examUserMapper.updateById(examUser);
    }

    public int deleteById(int cuid) {
        return examUserMapper.deleteById(cuid);
    }

    public int deleteByContestId(int contestId) {
        QueryWrapper<ExamUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExamUser::getContestId, contestId);
        return examUserMapper.delete(queryWrapper);
    }


    public ExamUser getExamUserById(int cuid) {
        return examUserMapper.selectById(cuid);
    }

    public List<ExamUser> getExamUserListByContestId(int contestId) {
        QueryWrapper<ExamUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExamUser::getContestId, contestId);
        return examUserMapper.selectList(queryWrapper);
    }

    public IPage<ExamUser> getExamUserPageByContestId(int contestId, int page, int size) {
        QueryWrapper<ExamUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExamUser::getContestId, contestId);
        Page<ExamUser> ipage = new Page<>(page, size);
        return examUserMapper.selectPage(ipage, queryWrapper);
    }

    public ExamUser getContestByUsername(String username) {
        QueryWrapper<ExamUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExamUser::getUsername, username);
        return examUserMapper.selectOne(queryWrapper);
    }

    public List<ExamRankVO> getExamRankByOiMode(int contestId){
        return examUserMapper.getExamRankByOiMode(contestId);
    }
    public List<ExamRankVO> getExamRankByAcmMode(int contestId){
        return examUserMapper.getExamRankByAcmMode(contestId);
    }
}

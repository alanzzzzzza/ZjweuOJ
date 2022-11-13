package com.nextrt.acm.biz.exercise;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.exercise.Submission;
import com.nextrt.core.mapper.exercise.SubmissionMapper;
import com.nextrt.core.vo.contest.SubmissionVO;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SubmissionBiz {
    private final SubmissionMapper submissionMapper;

    public SubmissionBiz(SubmissionMapper submissionMapper) {
        this.submissionMapper = submissionMapper;
    }

    public int addSubmission(Submission submission) {
        return submissionMapper.insert(submission);
    }

    public void updateStatus(int contestId,int problemId){
        submissionMapper.updateStatus(contestId, problemId);
    }

    public int updateSubmission(Submission submission) {
        return submissionMapper.updateById(submission);
    }

    public Submission getSubmissionById(int id) {
        return submissionMapper.selectById(id);
    }

    public List<Submission> getUnJudgeList() {
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Submission::getStatus, -1);
        return submissionMapper.selectList(queryWrapper);
    }

    public int deleteSubmissionByContestId(int contestId){
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Submission::getContestId, contestId);
        return submissionMapper.delete(queryWrapper);
    }

    public int deleteSubmissionByContestIdAndUserId(int contestId,int userId){
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Submission::getContestId, contestId).eq(Submission::getUserId,userId);
        return submissionMapper.delete(queryWrapper);
    }

    public IPage<SubmissionVO> TouristsGetSubmission(int problemId, int userId, int page, int size) {
        Page<Submission> aPage = new Page<Submission>(page, size);
        return submissionMapper.TouristsGetSubmission(aPage, problemId,userId);
    }

    public IPage<SubmissionVO> AdminGetSubmission(int problemId, int userId, int page, int size) {
        Page<Submission> aPage = new Page<Submission>(page, size);
        return submissionMapper.AdminGetSubmission(aPage, problemId,userId);
    }

    public IPage<SubmissionVO> GetContestSubmission(int problemId, int contestId, int page, int size) {
        Page<SubmissionVO> aPage = new Page<>(page, size);
        return submissionMapper.GetContestSubmission(aPage, problemId,contestId);
    }
    public IPage<SubmissionVO> UserGetSubmission(int contestId, int problemId, int userId, int page, int size) {
        Page<SubmissionVO> aPage = new Page<>(page, size);
        return submissionMapper.UserGetContestSubmission(aPage,contestId,problemId,userId);
    }

    public IPage<SubmissionVO> contestGetUserSubmission(int contestId, int problemId, int userId, int page, int size) {
        Page<SubmissionVO> aPage = new Page<>(page, size);
        return submissionMapper.contestGetUserContestSubmission(aPage,contestId,problemId,userId);
    }
    public IPage<SubmissionVO> contestGetAllSubmission(int contestId, int problemId, int page, int size) {
        Page<SubmissionVO> aPage = new Page<>(page, size);
        return submissionMapper.contestGetAllContestSubmission(aPage,contestId,problemId);
    }


    public List<SubmissionVO> contestGetUserSubmission(int contestId, int problemId, int userId) {
        return submissionMapper.contestAdminGetUserContestSubmission(contestId,problemId,userId);
    }
    public List<SubmissionVO> contestGetAllSubmission(int contestId, int problemId) {
        return submissionMapper.contestAdminGetAllContestSubmission(contestId,problemId);
    }


    public IPage<SubmissionVO> examGetUserSubmission(int contestId, int problemId, int userId, int page, int size) {
        Page<SubmissionVO> aPage = new Page<>(page, size);
        return submissionMapper.examGetUserContestSubmission(aPage,contestId,problemId,userId);
    }
    public IPage<SubmissionVO> examGetAllSubmission(int contestId, int problemId, int page, int size) {
        Page<SubmissionVO> aPage = new Page<>(page, size);
        return submissionMapper.examGetAllContestSubmission(aPage,contestId,problemId);
    }


    public List<SubmissionVO> examGetUserSubmission(int contestId, int problemId, int userId) {
        return submissionMapper.examAdminGetUserContestSubmission(contestId,problemId,userId);
    }
    public List<SubmissionVO> examGetAllSubmission(int contestId, int problemId) {
        return submissionMapper.examAdminGetAllContestSubmission(contestId,problemId);
    }

    public IPage<SubmissionVO> getOrdinaryUserSubmission(int problemId, int userId, int page, int size) {
        Page<SubmissionVO> aPage = new Page<SubmissionVO>(page, size);
        return submissionMapper.getOrdinaryUserSubmission(aPage,problemId,userId);
    }

    /**
     * @Description: 获取用户提交数据
     * @Param: type : 1用户提交数 2用户通过数 3用户参与题目数  4用户通过问题数
     * @return:
     * @Author: 轩辕子墨
     * @date: 2020/2/25
     */
    public int ordinaryCountNum(int userId, int type) {
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Submission::getUserId, userId).eq(Submission::getContestId,0);
        switch (type) {
            case 2:
                queryWrapper.lambda().eq(Submission::getStatus, 0);
                break;
            case 3:
                queryWrapper.select("DISTINCT problem_id");
                break;
            case 4:
                queryWrapper.lambda().eq(Submission::getStatus, 0);
                queryWrapper.select("DISTINCT problem_id");
                break;
            default:
                break;
        }
        return submissionMapper.selectCount(queryWrapper);
    }

    //2 解决数  1提交数
    public int countContestProblemNum(int contestId,int problemId,int type) {
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Submission::getContestId, contestId).eq(Submission::getProblemId,problemId);
        switch (type) {
            case 2:
                queryWrapper.lambda().eq(Submission::getStatus, 0);
                queryWrapper.select("DISTINCT user_id");
                break;
            default:
                break;
        }
        return submissionMapper.selectCount(queryWrapper);
    }

    //2 解决数  1提交数
    public int countContestUserDataNum(int contestId,int userId,int type) {
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Submission::getContestId, contestId).eq(Submission::getUserId,userId);
        switch (type) {
            case 2:
                queryWrapper.lambda().eq(Submission::getStatus, 0);
                queryWrapper.select("DISTINCT problem_id");
                break;
            default:
                break;
        }
        return submissionMapper.selectCount(queryWrapper);
    }
}

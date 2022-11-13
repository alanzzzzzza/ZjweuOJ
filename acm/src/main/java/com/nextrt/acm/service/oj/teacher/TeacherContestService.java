package com.nextrt.acm.service.oj.teacher;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.acm.biz.contest.*;
import com.nextrt.acm.biz.exam.ExamUserBiz;
import com.nextrt.acm.biz.exercise.SubmissionBiz;
import com.nextrt.acm.config.thread.ViThreadPoolManager;
import com.nextrt.acm.service.SubmissionService;
import com.nextrt.core.entity.contest.Contest;
import com.nextrt.core.entity.contest.ContestIPReport;
import com.nextrt.core.entity.contest.ContestProblem;
import com.nextrt.core.entity.contest.ContestProblemResult;
import com.nextrt.core.entity.exam.ExamUser;
import com.nextrt.core.vo.Result;
import com.nextrt.core.vo.contest.ContestJoinVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
//管理员竞赛模块
public class TeacherContestService {

    private final ContestBiz contestBiz;
    private final ContestProblemBiz contestProblemBiz;
    private final ContestJoinInfoBiz joinInfoBiz;
    private final ContestReportIPBiz reportIPBiz;
    private final ExamUserBiz examUserBiz;
    private final ContestProblemResultBiz resultBiz;
    private final SubmissionBiz submissionBiz;
    @Autowired
    private SubmissionService submissionService;

    public TeacherContestService(ContestBiz contestBiz, ContestProblemBiz contestProblemBiz, ContestJoinInfoBiz joinInfoBiz, ContestReportIPBiz reportIPBiz, ExamUserBiz examUserBiz, ContestProblemResultBiz resultBiz, SubmissionBiz submissionBiz) {
        this.contestBiz = contestBiz;
        this.contestProblemBiz = contestProblemBiz;
        this.joinInfoBiz = joinInfoBiz;
        this.reportIPBiz = reportIPBiz;
        this.examUserBiz = examUserBiz;
        this.resultBiz = resultBiz;
        this.submissionBiz = submissionBiz;
    }

    public Result reJudge(int contestId, int problemId, int userId) {
        Contest old = contestBiz.getContestById(contestId);
        if (old.getUserId() != userId) {
            return new Result(-1, "权限不足，请确认该竞赛创建人是本帐号");
        }
        submissionBiz.updateStatus(contestId, problemId);
        submissionService.dealSubmission();
        return new Result(1, "重新判题请求已经提交");
    }


    //添加竞赛
    public Result addContest(Contest contest) {
        contest.setRoomId(RandomUtil.randomStringUpper(8));
        if (contestBiz.addContest(contest) > 0) {
            if (!contest.getProblemIds().isEmpty())
                contestProblemBiz.addContestProblem(contest.getProblemIds(), contest.getContestId());//插入问题信息
            return new Result(1, "竞赛添加成功！");
        } else
            return new Result(0, "竞赛添加失败，请联系管理员!");
    }

    //更新竞赛
    public Result updateContest(Contest contest) {
        Contest old = contestBiz.getContestById(contest.getContestId());
        if (!old.getUserId().equals(contest.getUserId())) return new Result(-1, "权限不足，请确认该竞赛创建人是本帐号");
        contest.setIsTestAccount(old.getIsTestAccount());
        contest.setType(old.getType());
        if (contestBiz.updateContest(contest) > 0) {
            ViThreadPoolManager.getInstance().execute(() -> {
                contestProblemBiz.updateContestProblem(contest.getProblemIds(), contest.getContestId());
            });
            return new Result(1, "竞赛更新成功！");
        } else
            return new Result(0, "竞赛更新失败，请联系管理员!");
    }

    //删除竞赛
    public Result deleteContest(int contestId, int userId) {
        Contest old = contestBiz.getContestById(contestId);
        if (old.getUserId() != userId) return new Result(-1, "权限不足，请确认该竞赛创建人是本帐号");
        if (contestBiz.deleteContest(contestId) > 0) {
            ViThreadPoolManager.getInstance().execute(() -> {
                examUserBiz.deleteByContestId(contestId);
                submissionBiz.deleteSubmissionByContestId(contestId);//删除提交数据
                contestProblemBiz.deleteAllContestProblem(contestId);//删除竞赛问题数据
                resultBiz.deleteByContestId(contestId);//删除问题结果
                joinInfoBiz.deleteByContestId(contestId);//删除竞赛报名信息
                reportIPBiz.deleteByContestId(contestId);//删除竞赛IP上报数据
            });
            return new Result(1, "竞赛删除成功！");
        } else
            return new Result(0, "竞赛删除失败，请联系管理员!");
    }

    public Result copyContest(int contestId, int userId) {
        Contest old = contestBiz.getContestById(contestId);
        if (old == null) return new Result(-1, "复制失败，竞赛不存在");
        if (old.getUserId() != userId) return new Result(-1, "权限不足，请确认该竞赛创建人是本帐号");
        old.setContestId(0);
        old.setRoomId(RandomUtil.randomStringUpper(8));
        old.setName(old.getName() + "-复制竞赛");
        if (contestBiz.addContest(old) > 0) {
            List<ContestProblem> list = contestProblemBiz.getContestProblemId(contestId);
            ViThreadPoolManager.getInstance().execute(() -> contestProblemBiz.updateContestProblem(list.stream().map(ContestProblem::getProblemId).collect(Collectors.toList()), old.getContestId()));
            return new Result(1, "复制竞赛成功");
        }
        return new Result(-1, "复制失败");
    }

    //获取系统中的竞赛列表
    public Result getContestList(String name, int userId, int page, int size) {
        return new Result(1, "竞赛列表获取成功", contestBiz.teacherGetContestListByPage(name, userId, page, size));
    }


    //获取竞赛详情
    public Result getContestInfo(int contestId, int userId) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest == null) return new Result(-1, "该竞赛不存在");
        if (contest.getUserId() != userId) return new Result(-1, "权限不足，请确认该竞赛创建人是本帐号");
        List<ContestProblem> list = contestProblemBiz.getContestProblemId(contestId);
        if (!list.isEmpty())
            contest.setProblemIds(list.stream().map(ContestProblem::getProblemId).collect(Collectors.toList()));
        return new Result(1, "竞赛信息获取成功", contest);
    }

    //获取竞赛报名信息
    public Result getContestJoinList(int page, int size, int userId, int contestId) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest == null) return new Result(-1, "该竞赛不存在");
        if (contest.getUserId() != userId) return new Result(-1, "权限不足，请确认该竞赛创建人是本帐号");
        IPage<ContestJoinVO> res = joinInfoBiz.getContestJoinVO(page, size, contestId);
        return new Result(1, "获取成功", res);
    }

    //获取用户竞赛答题信息
    public Result getUserContestResult(int contestId, int userId, int getUserId) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest == null) return new Result(-1, "该竞赛不存在");
        if (contest.getUserId() != userId) return new Result(-1, "权限不足，请确认该竞赛创建人是本帐号");
        List<ContestProblemResult> list = resultBiz.getUserContestProblemResult(getUserId, contestId);
        return new Result(1, "获取用户答题情况成功!", list);
    }

    //获取竞赛排行榜
    public Result getContestRank(int contestId, int userId) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest == null) return new Result(-1, "竞赛不存在!");
        if (contest.getUserId() != userId) return new Result(-1, "权限不足，请确认该竞赛创建人是本帐号");
        if (contest.getIsTestAccount() == 0) {
            if (contest.getType() == 1)
                return new Result(1, "获取成功!", joinInfoBiz.getContestRankByOi(contestId));
            else
                return new Result(1, "获取成功!", joinInfoBiz.getContestRankByAcm(contestId));
        } else {
            if (contest.getType() == 1)
                return new Result(1, "获取成功!", examUserBiz.getExamRankByOiMode(contestId));
            else
                return new Result(1, "获取成功!", examUserBiz.getExamRankByAcmMode(contestId));
        }
    }

    //获取竞赛的问题列表
    public Result getProblemsByContestId(int contestId, int userId) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest == null) return new Result(-1, "竞赛不存在!");
        if (contest.getUserId() != userId) return new Result(-1, "权限不足，请确认该竞赛创建人是本帐号");
        return new Result(1, "竞赛题目数据获取成功", contestProblemBiz.adminGetContestProblemListById(contestId));
    }

    public List<ContestIPReport> getUserIPReportList(int contestId, int userId, int getUserId) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest == null || contest.getUserId() != userId) return null;
        return reportIPBiz.getUserContestIpList(getUserId, contestId);
    }

    public List<Map<String, Object>> getContestResultExcel(int contestId, int userId) {
        Contest contest = contestBiz.getContestById(contestId);
        if (contest == null || contest.getUserId() != userId) return null;
        List<ContestProblemResult> contestProblemResults = resultBiz.getContestProblemResultList(contestId);
        List<Map<String, Object>> list = new ArrayList<>();
        if (contest.getIsTestAccount() == 1) {
            //测试帐号
            List<ExamUser> examUserList = examUserBiz.getExamUserListByContestId(contestId);
            examUserList.forEach(x -> {
                Map<String, Object> temp = new LinkedHashMap<>();
                temp.put("Id", x.getContestUserId());
                temp.put("用户名", x.getUsername());
                temp.put("姓名", x.getName());
                temp.put("学号", x.getSchoolNo());
                temp.put("队伍", x.getTeam());
                temp.put("学校", x.getSchool());
                temp.put("班级", x.getClassName());
                temp.put("总提交数", x.getSub());
                temp.put("AC数", x.getAc());
                temp.put("加入时间", x.getJoinTime());
                temp.put("开始答题时间", x.getStartTime());
                temp.put("截止作答时间", x.getEndTime());
                temp.put("IP是否存在变动", x.getSuspicious() == 1 ? "可疑" : "正常");
                temp.put("总得分", x.getScore());
                contestProblemResults.forEach(s -> {
                    if (s.getUserId() == x.getContestUserId()) {
                        temp.put(s.getProblemId() + ":" + s.getProblemName(), s.getScore());
                    }
                });
                list.add(temp);
            });
        } else {
            List<ContestJoinVO> contestJoinVOList = joinInfoBiz.getContestJoinVO(contestId);
            contestJoinVOList.forEach(x -> {
                Map<String, Object> temp = new LinkedHashMap<>();
                temp.put("Id", x.getUserId());
                temp.put("用户名", x.getUsername());
                temp.put("昵称", x.getNick());
                temp.put("邮箱", x.getEmail());
                temp.put("总提交数", x.getSub());
                temp.put("AC数", x.getAc());
                temp.put("加入时间", x.getJoinTime());
                temp.put("开始答题时间", x.getStartTime());
                temp.put("截止作答时间", x.getEndTime());
                temp.put("IP是否存在变动", x.getSuspicious() == 1 ? "可疑" : "正常");
                temp.put("总得分", x.getScore());
                contestProblemResults.forEach(s -> {
                    if (s.getUserId() == x.getUserId()) {
                        temp.put(s.getProblemId() + ":" + s.getProblemName(), s.getScore());
                    }
                });
                list.add(temp);
            });
        }
        return list;
    }


}

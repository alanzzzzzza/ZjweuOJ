package com.nextrt.acm.service.oj.exam;

import com.nextrt.acm.biz.contest.*;
import com.nextrt.acm.biz.exam.ExamUserBiz;
import com.nextrt.acm.config.thread.ViThreadPoolManager;
import com.nextrt.acm.service.SubmissionService;
import com.nextrt.acm.util.RedisUtil;
import com.nextrt.core.entity.contest.Contest;
import com.nextrt.core.entity.contest.ContestIPReport;
import com.nextrt.core.entity.exam.ExamUser;
import com.nextrt.core.vo.Result;
import com.nextrt.core.vo.contest.ExamContestInfo;
import com.nextrt.core.vo.contest.ExamRankVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.nextrt.acm.util.NetUtil.checkContestIP;

@Service
public class ExamContestService {

    private final ContestBiz contestBiz;
    private final ContestProblemBiz contestProblemBiz;
    private final ContestJoinInfoBiz joinInfoBiz;
    private final ContestReportIPBiz reportIPBiz;
    private final ContestProblemResultBiz resultBiz;
    private final SubmissionService submissionService;
    private final RedisUtil redisUtil;
    @Autowired
    private ExamUserBiz examUserBiz;

    public ExamContestService(ContestBiz contestBiz, ContestProblemBiz contestProblemBiz, ContestJoinInfoBiz joinInfoBiz, ContestReportIPBiz reportIPBiz, ContestProblemResultBiz resultBiz, SubmissionService submissionService, RedisUtil redisUtil) {
        this.contestBiz = contestBiz;
        this.contestProblemBiz = contestProblemBiz;
        this.joinInfoBiz = joinInfoBiz;
        this.reportIPBiz = reportIPBiz;
        this.resultBiz = resultBiz;
        this.submissionService = submissionService;
        this.redisUtil = redisUtil;
    }

    //开始加入竞赛
    public Result start(int contestId, int userId, String localIP, String publicIP) {
        String lockKey = "StartContest-" + contestId + "-" + userId;
        Contest contest = contestBiz.getContestById(contestId);
        if (contest == null)
            return new Result(-1, "该竞赛不存在");
        if (contest.getIsTestAccount() != 1)
            return new Result(-1, "抱歉该竞赛不对测试帐号开放");
        ExamUser examUser = examUserBiz.getExamUserById(userId);
        if (examUser.getStartTime() != null)
            return new Result(0, "你已经加入竞赛，请勿重复加入");
        if (checkContestIP(publicIP, contest.getPublicIpRemark()) == 0)
            return new Result(-1, "您当前的公网IP不在允许范围内!");
        if (contest.getStartTime().getTime() > System.currentTimeMillis())
            return new Result(-1, "抱歉竞赛还没开始");
        if (contest.getEndTime().getTime() < System.currentTimeMillis())
            return new Result(-1, "竞赛已结束不允许加入");
        examUser.setStartTime(new Date());
        examUser.setStartPublicIp(publicIP);
        examUser.setStartLocalIp(localIP);
        examUser.setEndTime(getEndTime(contest, examUser.getStartTime()));
        if (redisUtil.getLock(lockKey)) {
            if (examUserBiz.update(examUser) > 0) {
                redisUtil.removeLock(lockKey);
                return new Result(1, "恭喜，您已成功开始竞赛！");
            }
        }
        return new Result(0, "系统错误，请联系管理员");
    }

    //竞赛时用户IP数据上报,并判断是否可疑
    public Result reportIP(ContestIPReport contestIPReport) {
        contestIPReport.setReportTime(new Date());
        reportIPBiz.add(contestIPReport);
        ViThreadPoolManager.getInstance().execute(() -> {
            ExamUser examUser = examUserBiz.getExamUserById(contestIPReport.getUserId());
            if (!examUser.getJoinLocalIp().equals(contestIPReport.getLocalIp()) || !examUser.getJoinPublicIp().equals(contestIPReport.getPublicIp())
                    || !examUser.getStartLocalIp().equals(contestIPReport.getLocalIp()) || !examUser.getStartPublicIp().equals(contestIPReport.getPublicIp())) {
                examUser.setSuspicious(1);//判定该用户考试情况可疑
                examUserBiz.update(examUser);
            } else {
                List<ContestIPReport> list = reportIPBiz.getUserContestIpList(contestIPReport.getUserId(), contestIPReport.getContestId());
                long localIpNum = list.stream().map(ContestIPReport::getLocalIp).distinct().count();
                long publicIPNum = list.stream().map(ContestIPReport::getPublicIp).distinct().count();
                if (localIpNum > 1 || publicIPNum > 1) {
                    examUser.setSuspicious(1);//判定该用户考试情况可疑
                    examUserBiz.update(examUser);
                }
            }
        });
        return new Result(1, "Success");
    }

    public Result getContestInfo(int contestId, int userId, String publicIP) {
        Contest contest = contestBiz.examGetContestInfo(contestId);
        if (contest == null) return new Result(-1, " 该竞赛不存在或者未开始!");
        if (checkContestIP(publicIP, contest.getPublicIpRemark()) == 0)
            return new Result(-1, "您当前的公网IP不在允许范围内!");
        if (contest.getEndVisible() == 0 && new Date().getTime() > contest.getEndTime().getTime())
            return new Result(-1, "由于竞赛管理者设置，竞赛在结束后不允许查看竞赛数据");
        ExamUser examUser = examUserBiz.getExamUserById(userId);
        if (examUser == null)
            return new Result(-1, "越权访问");
        ExamContestInfo examContestInfo = new ExamContestInfo();
        examContestInfo.setContest(contest);
        examContestInfo.setExamUser(examUser);
        return new Result(1, "竞赛信息获取成功", examContestInfo);
    }


    public Result getProblemsByContestId(int contestId, int userId, String publicIP) {
        Contest contest = contestBiz.examGetContestInfo(contestId);
        if (contest == null) return new Result(-1, " 不存在该竞赛!");
        ExamUser examUser = examUserBiz.getExamUserById(userId);
        if (examUser.getStartTime() == null)
            return new Result(-1, "抱歉，你尚未开始竞赛！");
        if (checkContestIP(publicIP, contest.getPublicIpRemark()) == 0)
            return new Result(-1, "您当前的公网IP不在允许范围内!");
        return new Result(1, "竞赛题目数据获取成功", contestProblemBiz.examGetProblemInfoByContestId(contestId, userId));
    }


    public Result getContestResult(int userId, int contestId) {
        return new Result(1, "获取成功!", resultBiz.getExamUserContestProblemResult(userId, contestId));
    }

    //获取排行榜
    public Result getContestRank(int contestId) {
        Contest contest = contestBiz.examGetContestInfo(contestId);
        if (contest == null) return new Result(-1, " 不存在该竞赛!");
        List<ExamRankVO> list = null;
        if (contest.getType() == 1)
            list = examUserBiz.getExamRankByOiMode(contestId);
        else
            list = examUserBiz.getExamRankByAcmMode(contestId);
        return new Result(1, "获取成功!", list);
    }

    //计算考试结束时间
    public Date getEndTime(Contest contest, Date startTime) {
        if (contest.getEndTime().getTime() > startTime.getTime() + contest.getTotalTime() * 60 * 1000)//
        {//当考试截止时间大于  开始时间加上总考试时间
            //当当前时间大于假设时间
            return new Date(startTime.getTime() + contest.getTotalTime() * 60 * 1000);
        } else {
            return new Date(contest.getEndTime().getTime() + 1000);
        }
    }

}

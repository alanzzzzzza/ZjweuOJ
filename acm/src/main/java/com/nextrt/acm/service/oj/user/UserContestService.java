package com.nextrt.acm.service.oj.user;

import com.nextrt.acm.biz.contest.*;
import com.nextrt.acm.config.thread.ViThreadPoolManager;
import com.nextrt.acm.service.SubmissionService;
import com.nextrt.acm.util.RedisUtil;
import com.nextrt.core.entity.contest.Contest;
import com.nextrt.core.entity.contest.ContestIPReport;
import com.nextrt.core.entity.contest.ContestJoinInfo;
import com.nextrt.core.entity.exercise.Submission;
import com.nextrt.core.vo.ContestInfo;
import com.nextrt.core.vo.Result;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.nextrt.acm.util.NetUtil.checkContestIP;

@Service
public class UserContestService {
    private final ContestBiz contestBiz;
    private final ContestProblemBiz contestProblemBiz;
    private final ContestJoinInfoBiz joinInfoBiz;
    private final ContestReportIPBiz reportIPBiz;
    private final ContestProblemResultBiz resultBiz;
    private final SubmissionService submissionService;
    private final RedisUtil redisUtil;

    public UserContestService(ContestBiz contestBiz, ContestProblemBiz contestProblemBiz, ContestJoinInfoBiz joinInfoBiz, ContestReportIPBiz reportIPBiz, ContestProblemResultBiz resultBiz, SubmissionService submissionService, RedisUtil redisUtil) {
        this.contestBiz = contestBiz;
        this.contestProblemBiz = contestProblemBiz;
        this.joinInfoBiz = joinInfoBiz;
        this.reportIPBiz = reportIPBiz;
        this.resultBiz = resultBiz;
        this.submissionService = submissionService;
        this.redisUtil = redisUtil;
    }

    //用户加入竞赛
    public Result addContestJoinInfo(ContestJoinInfo contestJoinInfo) {
        String lockKey = "ContestJoinInfo-" + contestJoinInfo.getContestId() + "-" + contestJoinInfo.getUserId();
        Contest contest = null;
        if (contestJoinInfo.getContestId() != null && contestJoinInfo.getContestId() > 0) {
            contest = contestBiz.getContestById(contestJoinInfo.getContestId());
            if (contest == null) return new Result(-1, "该竞赛不存在!");
            if (contest.getIsPublic() == 0)
                return new Result(-1, "抱歉该竞赛不对外开放");
        } else {
            contest = contestBiz.getContestByRoomId(contestJoinInfo.getRoomId());
            if (contest == null) return new Result(-1, "该竞赛不存在!");
            if (!contestJoinInfo.getPassword().equals(contest.getPassword()))
                return new Result(-1, "房间密码出错，请检查！");
            contestJoinInfo.setContestId(contest.getContestId());
        }
        if (contest.getIsTestAccount() == 1)
            return new Result(-1, "该竞赛只允许测试帐号参加");
        if (checkContestIP(contestJoinInfo.getJoinPublicIp(), contest.getPublicIpRemark()) == 0)
            return new Result(-1, "您当前的公网IP不在允许范围内!");
        if (joinInfoBiz.getContestJoinInfo(contestJoinInfo.getContestId(), contestJoinInfo.getUserId()) == null) {
            if (contest.getStartTime().getTime() > System.currentTimeMillis())
                return new Result(-1, "竞赛还没开始");
            if (contest.getEndTime().getTime() < System.currentTimeMillis())
                return new Result(-1, "竞赛已结束不允许加入");
            if (redisUtil.getLock(lockKey)) {
                if (joinInfoBiz.add(contestJoinInfo) > 0) {
                    redisUtil.removeLock(lockKey);
                    return new Result(1, "恭喜，您已成功加入竞赛！");
                }
            }
        } else {
            return new Result(2, "你已加入该竞赛，请勿重复申请！");
        }
        return new Result(0, "系统错误，请联系管理员");
    }

    //用户获取公开竞赛列表
    public Result getContestList(String name, int page, int size) {
        return new Result(1, "竞赛列表获取成功", contestBiz.userGetContestListByPage(name, page, size));
    }

    //用户获取已经加入竞赛列表
    public Result getJoinContestList(int userId, int page, int size) {
        return new Result(1, "获取成功", joinInfoBiz.getUserJoinContestList(userId, page, size));
    }

    //竞赛时用户IP数据上报
    public Result reportIP(ContestIPReport contestIPReport) {
        contestIPReport.setReportTime(new Date());
        reportIPBiz.add(contestIPReport);
        ViThreadPoolManager.getInstance().execute(() -> {
            ContestJoinInfo contestJoinInfo = joinInfoBiz.getContestJoinInfo(contestIPReport.getContestId(), contestIPReport.getUserId());
            if (!contestJoinInfo.getJoinLocalIp().equals(contestIPReport.getLocalIp()) || !contestJoinInfo.getJoinPublicIp().equals(contestIPReport.getPublicIp())
                    || !contestJoinInfo.getStartLocalIp().equals(contestIPReport.getLocalIp()) || !contestJoinInfo.getStartPublicIp().equals(contestIPReport.getPublicIp())) {
                contestJoinInfo.setSuspicious(1);//判定该用户考试情况可疑
                joinInfoBiz.updateContestJoinInfo(contestJoinInfo);
            } else {
                List<ContestIPReport> list = reportIPBiz.getUserContestIpList(contestIPReport.getUserId(), contestIPReport.getContestId());
                long localIpNum = list.stream().map(ContestIPReport::getLocalIp).distinct().count();
                long publicIPNum = list.stream().map(ContestIPReport::getPublicIp).distinct().count();
                if (localIpNum > 1 || publicIPNum > 1) {
                    contestJoinInfo.setSuspicious(1);//判定该用户考试情况可疑
                    joinInfoBiz.updateContestJoinInfo(contestJoinInfo);
                }
            }
        });
        return new Result(1, "Success");
    }

    public Result userGetContestInfo(int contestId, int userId, String localIP, String publicIP) {
        Contest contest = contestBiz.userGetContestInfo(contestId);
        if (contest == null) return new Result(-1, " 该竞赛不存在或者未开始!");
        ContestJoinInfo contestJoinInfo = joinInfoBiz.getContestJoinInfo(contestId, userId);
        if (contestJoinInfo == null)
            return new Result(-1, "抱歉，你未加入该竞赛！");
        if (checkContestIP(publicIP, contest.getPublicIpRemark()) == 0)
            return new Result(-1, "您当前的公网IP不在允许范围内!");
        if (contest.getEndVisible() == 0 && new Date().getTime() > contest.getEndTime().getTime())
            return new Result(-1, "由于竞赛管理者设置，竞赛在结束后不允许查看竞赛数据");
        if (contestJoinInfo.getJoinLocalIp() == null) {
            ViThreadPoolManager.getInstance().execute(() -> {
                contestJoinInfo.setJoinLocalIp(localIP);
                contestJoinInfo.setJoinPublicIp(publicIP);
                joinInfoBiz.updateContestJoinInfo(contestJoinInfo);
            });
        }
        ContestInfo contestInfo = new ContestInfo();
        contest.setPublicIpRemark("");
        contestInfo.setContest(contest);
        contestInfo.setContestJoinInfo(contestJoinInfo);
        return new Result(1, "竞赛信息获取成功", contestInfo);
    }

    public Result startContest(int contestId, int userId, String localIP, String publicIP) {
        Contest contest = contestBiz.userGetContestInfo(contestId);
        if (contest == null) return new Result(-1, " 不存在该竞赛!");
        ContestJoinInfo contestJoinInfo = joinInfoBiz.getContestJoinInfo(contestId, userId);
        if (contestJoinInfo == null) return new Result(-1, "抱歉，你未加入该竞赛！");
        if (checkContestIP(publicIP, contest.getPublicIpRemark()) == 0) return new Result(-1, "您当前的公网IP不在允许范围内!");
        if (contestJoinInfo.getStartTime() == null) {
            contestJoinInfo.setStartTime(new Date());
            contestJoinInfo.setStartLocalIp(localIP);
            contestJoinInfo.setStartPublicIp(publicIP);
            contestJoinInfo.setEndTime(getEndTime(contest, contestJoinInfo.getStartTime()));
            if (joinInfoBiz.updateContestJoinInfo(contestJoinInfo) > 0)
                return new Result(1, "您已成功开启竞赛");
            return new Result(-1, "系统出错，请练习管理员");
        } else {
            return new Result(2, "你已经开始竞赛了请勿重复开启");
        }
    }

    public Result userGetProblemsByContestId(int contestId, int userId, String publicIP) {
        Contest contest = contestBiz.userGetContestInfo(contestId);
        if (contest == null) return new Result(-1, " 不存在该竞赛!");
        ContestJoinInfo contestJoinInfo = joinInfoBiz.getContestJoinInfo(contestId, userId);
        if (contestJoinInfo == null)
            return new Result(-1, "抱歉，你未加入该竞赛！");
        if (checkContestIP(publicIP, contest.getPublicIpRemark()) == 0)
            return new Result(-1, "您当前的公网IP不在允许范围内!");
        if (contestJoinInfo.getStartTime() == null)
            return new Result(-1, "您未开始竞赛，请先开启竞赛。");
        return new Result(1, "竞赛题目数据获取成功", contestProblemBiz.userGetProblemInfoByContestId(contestId, userId));
    }

    public Result contestSubmission(Submission submission) {
        Contest contest = contestBiz.getContestById(submission.getContestId());
        if (contest == null) return new Result(-1, " 不存在该竞赛!");
        if (checkContestIP(submission.getPublicIp(), contest.getPublicIpRemark()) == 0)
            return new Result(-1, "您当前的公网IP不在允许范围内!");
        ContestJoinInfo contestJoinInfo = joinInfoBiz.getContestJoinInfo(submission.getContestId(), submission.getUserId());
        if (contestJoinInfo == null)
            return new Result(-1, "抱歉，您没有权限访问该竞赛！");
        List<String> language = Arrays.asList(contest.getLanguage().split(","));
        if (!language.contains(submission.getLanguage()))
            return new Result(-1, "当前语言不被支持");
        if (System.currentTimeMillis() - 60 * 1000 > contestJoinInfo.getEndTime().getTime())
            return new Result(-1, "竞赛已经截止，无法提交答案！");
        if (submissionService.addSubmission(submission) == 1) {
            return new Result(1, "题目提交成功,正在判题!");
        } else {
            return new Result(-1, "系统出错请联系管理员!");
        }
    }

    public Result getContestResult(int contestId, int userId, String publicIP) {
        Contest contest = contestBiz.userGetContestInfo(contestId);
        if (contest == null) return new Result(-1, " 该竞赛不存在或者未开始!");
        ContestJoinInfo contestJoinInfo = joinInfoBiz.getContestJoinInfo(contestId, userId);
        if (contestJoinInfo == null)
            return new Result(-1, "抱歉，你未加入该竞赛！");
        if (contest.getEndVisible() == 0 && new Date().getTime() > contest.getEndTime().getTime())
            return new Result(-1, "由于竞赛管理者设置，竞赛在结束后不允许查看竞赛数据");
        if (checkContestIP(publicIP, contest.getPublicIpRemark()) == 0)
            return new Result(-1, "您当前的公网IP不在允许范围内!");
        return new Result(1, "获取成功!", resultBiz.getUserContestProblemResult(userId, contestId));
    }

    public Result getContestRank(int contestId, int userId, String publicIP) {
        Contest contest = contestBiz.userGetContestInfo(contestId);
        if (contest == null) return new Result(-1, " 该竞赛不存在或者未开始!");
        ContestJoinInfo contestJoinInfo = joinInfoBiz.getContestJoinInfo(contestId, userId);
        if (contestJoinInfo == null)
            return new Result(-1, "抱歉，你未加入该竞赛！");
        if (contest.getEndVisible() == 0 && new Date().getTime() > contest.getEndTime().getTime())
            return new Result(-1, "由于竞赛管理者设置，竞赛在结束后不允许查看竞赛数据");
        if (checkContestIP(publicIP, contest.getPublicIpRemark()) == 0)
            return new Result(-1, "您当前的公网IP不在允许范围内!");
        if (contest.getIsTestAccount() == 0) {
            if (contest.getType() == 1)
                return new Result(1, "获取成功!", joinInfoBiz.getContestRankByOi(contestId));
            else
                return new Result(1, "获取成功!", joinInfoBiz.getContestRankByAcm(contestId));
        }
        return new Result(-1, "该竞赛不对普通用户开放");
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

package com.nextrt.acm.service.oj.admin;

import com.nextrt.acm.biz.contest.ContestBiz;
import com.nextrt.acm.biz.contest.ContestJoinInfoBiz;
import com.nextrt.acm.biz.contest.ContestProblemResultBiz;
import com.nextrt.acm.biz.contest.ContestReportIPBiz;
import com.nextrt.acm.biz.exercise.SubmissionBiz;
import com.nextrt.acm.biz.user.UserBiz;
import com.nextrt.acm.util.RedisUtil;
import com.nextrt.core.entity.contest.Contest;
import com.nextrt.core.entity.contest.ContestJoinInfo;
import com.nextrt.core.entity.user.User;
import com.nextrt.core.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOrdinaryService {
    @Autowired
    private ContestJoinInfoBiz joinInfoBiz;
    @Autowired
    private UserBiz userBiz;
    @Autowired
    private ContestProblemResultBiz resultBiz;
    @Autowired
    private SubmissionBiz submissionBiz;
    @Autowired
    private ContestReportIPBiz reportIPBiz;
    @Autowired
    private ContestBiz contestBiz;
    @Autowired
    private RedisUtil redisUtil;

    public Result deleteUserJoin(int userId, int contestId) {
        if (joinInfoBiz.deleteByContestIdAndUserId(userId, contestId) > 0) {
            submissionBiz.deleteSubmissionByContestIdAndUserId(contestId,userId);//删除提交数据
            resultBiz.deleteByContestIdAndUserId(contestId,userId);//删除问题结果
            reportIPBiz.deleteByContestIdAndUserId(contestId,userId);//删除竞赛IP上报数据
            return new Result(1, "删除成功");
        } else
            return new Result(-1, "删除失败");
    }

    public Result addJoinUser(String data, int type, int contestId) {
        User user = null;
        List<User> list = null;
        switch (type) {
            case 1://用户名
                user = userBiz.getUserByUsername(data);
                break;
            case 2://邮箱
                user = userBiz.getUserByEmail(data);
                break;
            case 3://用户名前缀
                list = userBiz.getUserByPrefix(data);
                break;
        }
        if (user != null) {
            ContestJoinInfo contestJoinInfo = new ContestJoinInfo();
            contestJoinInfo.setContestId(contestId);
            contestJoinInfo.setUserId(user.getUserId());
            return addContestJoinInfo(contestJoinInfo);
        }
        if (list != null && list.size() > 0) {
            list.forEach(x -> {
                ContestJoinInfo contestJoinInfo = new ContestJoinInfo();
                contestJoinInfo.setContestId(contestId);
                contestJoinInfo.setUserId(x.getUserId());
                addContestJoinInfo(contestJoinInfo);
            });
            return new Result(1, "加入用户完成");
        }
        return new Result(-1, "找不到用户信息");
    }

    public Result addContestJoinInfo(ContestJoinInfo contestJoinInfo) {
        String lockKey = "ContestJoinInfo-" + contestJoinInfo.getContestId() + "-" + contestJoinInfo.getUserId();
        Contest contest = contestBiz.getContestById(contestJoinInfo.getContestId());
        if (contest.getIsTestAccount() == 1) return new Result(-1, "该竞赛只允许测试帐号参加");
        if (joinInfoBiz.getContestJoinInfo(contestJoinInfo.getContestId(), contestJoinInfo.getUserId()) == null) {
            if (contest.getEndTime().getTime() < System.currentTimeMillis())
                return new Result(-1, "竞赛已结束不允许加入");
            if (redisUtil.getLock(lockKey)) {
                if (joinInfoBiz.add(contestJoinInfo) > 0) {
                    redisUtil.removeLock(lockKey);
                    return new Result(1, "该用户已成功加入竞赛！");
                }
            }
        } else {
            return new Result(2, "该帐号已加入该竞赛，请勿重复申请！");
        }
        return new Result(0, "系统错误，请联系管理员");
    }

}

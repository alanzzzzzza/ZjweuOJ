package com.nextrt.acm.biz.contest;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nextrt.core.entity.contest.ContestIPReport;
import com.nextrt.core.mapper.contest.ContestIPReportMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ContestReportIPBiz {
    private final ContestIPReportMapper reportMapper;

    public ContestReportIPBiz(ContestIPReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    @Async
    public void add(ContestIPReport contestIPReport) {
        reportMapper.insert(contestIPReport);
    }

    public List<ContestIPReport> getUserContestIpList(int userId,int contestId){
        QueryWrapper<ContestIPReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestIPReport::getContestId,contestId).eq(ContestIPReport::getUserId,userId);
        return reportMapper.selectList(queryWrapper);
    }

    @Async
    //删除竞赛关联的IP上报数据
    public void deleteByContestId(int contestId){
        QueryWrapper<ContestIPReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestIPReport::getContestId,contestId);
        reportMapper.delete(queryWrapper);
    }
    //管理员删除所有Ip上报数据
    public int deleteIpReportHistoryByAdmin(){
        QueryWrapper<ContestIPReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().le(ContestIPReport::getReportTime,new Date());
        return reportMapper.delete(queryWrapper);
    }

    @Async
    //系统自动删除多少日前的Ip上报数据
    public void deleteIpReportHistoryByCrontab(int day){
        QueryWrapper<ContestIPReport> queryWrapper = new QueryWrapper<>();
        Date date =  DateUtil.offsetDay(new Date(), -day);
        queryWrapper.lambda().le(ContestIPReport::getReportTime,date);
        reportMapper.delete(queryWrapper);
    }

    @Async
    //删除竞赛关联的IP上报数据
    public void deleteByContestIdAndUserId(int contestId,int userId){
        QueryWrapper<ContestIPReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContestIPReport::getContestId,contestId).eq(ContestIPReport::getUserId,userId);
        reportMapper.delete(queryWrapper);
    }
    @Async
    //删除多少天前的IP上报数据
    public void deleteByHistory(int day){
        QueryWrapper<ContestIPReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().le(ContestIPReport::getReportTime,new Date(System.currentTimeMillis()-1000*60*60*24*day));
        reportMapper.delete(queryWrapper);
    }

}

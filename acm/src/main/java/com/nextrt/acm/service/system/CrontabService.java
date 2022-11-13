package com.nextrt.acm.service.system;

import com.nextrt.acm.biz.contest.ContestReportIPBiz;
import com.nextrt.acm.config.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CrontabService {
    private final ContestReportIPBiz reportIPBiz;
    private final SystemConfig config;

    public CrontabService(ContestReportIPBiz reportIPBiz, SystemConfig config) {
        this.reportIPBiz = reportIPBiz;
        this.config = config;
    }

    @Async
    @Scheduled(cron = "1 0 0 * * *")
    public void deleteIpReportHistoryByAdmin() {
        reportIPBiz.deleteIpReportHistoryByCrontab(config.getInt("AutoDeleteReportIPTime"));
    }
}

package com.nextrt.acm.service.system;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.nextrt.acm.config.SystemConfig;
import com.nextrt.acm.util.ExecutorUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//数据库备份服务
@Service
public class SQLBackupService {
    private static final String ipPattern = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
    private static final String portPattern = "[^:][0-9]+(?=/)";
    private static final String databaseNamePattern = "[^/][0-9A-Za-z]+(?=\\?)";
    private final SystemConfig config;
    private final EmailService emailService;
    @Value("${spring.datasource.url}")
    private String sqlUrl;
    @Value("${spring.datasource.username}")
    private String sqlUsername;
    @Value("${spring.datasource.password}")
    private String sqlPassword;
    private String sqlIP = "127.0.0.1";
    private String sqlPort = "3306";
    private String sqlName = "acm";

    public SQLBackupService(SystemConfig config, EmailService emailService) {
        this.config = config;
        this.emailService = emailService;
    }

    public void initData() {
        Pattern r = Pattern.compile(ipPattern);
        Matcher m = r.matcher(sqlUrl);
        if (m.find()) sqlIP = m.group();
        r = Pattern.compile(portPattern);
        m = r.matcher(sqlUrl);
        if (m.find()) sqlPort = m.group();
        r = Pattern.compile(databaseNamePattern);
        m = r.matcher(sqlUrl);
        if (m.find()) sqlName = m.group();
    }

    @Scheduled(cron = "1 0 0 * * *")
    public void backupDataBase() {
        initData();
        String exportPath = config.getString("DataBaseBackupPath");
        String template = "mysqldump -h{} -u{} -p{} -P{} {} > {}";
        FileUtil.mkdir(exportPath + File.separator + DateUtil.today());
        String filename = exportPath + File.separator + DateUtil.today() + File.separator + System.currentTimeMillis() + ".sql";
        String cmd = StrUtil.format(template, sqlIP, sqlUsername, sqlPassword, sqlPort, sqlName, filename);
        ExecutorUtil.execs(cmd, 1000000);
        Map<String, String> files = new HashMap<>();
        files.put(DateUtil.today() + "-SQL-Backup.sql", filename);
        if(StrUtil.isNotBlank(config.getString("AdminEmail")))
            emailService.sendFile(config.getString("AdminEmail"), config.getString("SiteName") + "-数据库备份", "管理员你好这是当前数据库备份数据", files);
    }

    public String exportDataBase() {
        initData();
        String exportPath = config.getString("DataBaseBackupPath");
        String template = "mysqldump -h{} -u{} -p{} -P{} {} > {}";
        FileUtil.mkdir(exportPath + File.separator + DateUtil.today());
        String filename = exportPath + File.separator + DateUtil.today() + File.separator + System.currentTimeMillis() + ".sql";
        String cmd = StrUtil.format(template, sqlIP, sqlUsername, sqlPassword, sqlPort, sqlName, filename);
        ExecutorUtil.execs(cmd, 1000000);
        return filename;
    }

}

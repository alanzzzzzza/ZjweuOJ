package com.nextrt.judge.handler;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.nextrt.judge.service.Security;
import com.nextrt.judge.util.ExecutorUtil;
import com.nextrt.judge.util.FileUtils;
import com.nextrt.judge.util.ZipUtils;
import com.nextrt.judge.vo.JudgeResult;
import com.nextrt.judge.vo.JudgeResultCase;
import com.nextrt.judge.vo.JudgeTask;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;

/**
 * Base Handler
 */
public abstract class Handler {
    /**
     * 'Accepted' 通过,
     * 'Presentation Error 输出格式错误',
     * 'Time Limit Exceeded 时间超出',
     * 'Memory Limit Exceeded 内存超出限制',
     * 'Wrong Answer 错误',
     * 'Runtime Error 运行时错误',
     * 'Output Limit Exceeded',
     * 'Compile Error 编译错误',
     * 'System Error 系统出错'
     */
    protected final int AC = 0;
    protected final int PE = 1;
    protected final int TLE = 2;
    protected final int MLE = 3;
    protected final int WA = 4;
    protected final int RE = 5;
    protected final int OLE = 6;
    protected final int CE = 7;
    protected final int SE = 8;
    protected final int AE = 9;

    @Value("${judge.judgePath}")
    private String judgePath;

    @Value("${judge.scriptPath}")
    private String script;

    @Value("${judge.download}")
    private String download;

    @Autowired
    private Security security;

    private boolean checkTask(JudgeTask task, JudgeResult result) {
        if (task.getProId() == null) {
            if (task.getInput() == null || task.getOutput() == null
                    || task.getInput().size() == 0 || task.getOutput().size() == 0) {
                result.setErrorMessage("测试数据不能为空!");
                result.setStatus(SE);
                return false;
            }
            if (task.getInput().size() != task.getOutput().size()) {
                result.setErrorMessage("测试数据组数不对应!");
                result.setStatus(SE);
                return false;
            }
        }
        if (task.getSrc() == null || task.getSrc().trim().equals("")) {
            result.setErrorMessage("测试代码不能为空!");
            result.setStatus(SE);
            return false;
        }
        if (task.getTimeLimit() == null || task.getMemoryLimit() == null) {
            result.setErrorMessage("时间消耗、空间消耗不能为空!");
            result.setStatus(SE);
            return false;
        }
        if (task.getTimeLimit() < 0 || task.getTimeLimit() > 10000) {
            result.setErrorMessage("时间消耗应在范围0-10s内!");
            result.setStatus(SE);
            return false;
        }
        if (task.getMemoryLimit() < 0 || task.getMemoryLimit() > 524288) {
            result.setErrorMessage("空间消耗应在范围524288kb内!");
            result.setStatus(SE);
            return false;
        }
        return true;
    }

    protected abstract void createSrc(JudgeTask task, File path) throws IOException;

    protected abstract ExecutorUtil.ExecMessage HandlerCompiler(File path);

    protected abstract String getRunCommand(File path);


    private boolean createWorkspace(JudgeTask task, JudgeResult result, File path) {
        try {
            if (!path.exists())
                path.mkdirs();
            if (task.getProId() == null) {//create input and output
                for (int i = 1; i <= task.getInput().size(); i++) {
                    File inFile = new File(path, i + ".in");
                    File outFile = new File(path, i + ".out");
                    inFile.createNewFile();
                    FileUtils.write(task.getInput().get(i - 1), inFile);
                    outFile.createNewFile();
                    FileUtils.write(task.getOutput().get(i - 1), outFile);
                }
            } else {//download the testData
                String param = download.replace("{ProId}", task.getProId().toString()).replace("PATH", path.getPath());
                ExecutorUtil.ExecMessage msg = ExecutorUtil.exec(param, 5000);
                if (msg.getError() == null || !msg.getError().contains("0K")) {
                    throw new IOException("文件目录出错！");
                }
                ZipUtils.unzip(path.getPath() + File.separator + "main.zip", path.getPath());
            }
            createSrc(task, path);
        } catch (IOException e) {
            result.setStatus(SE);
            result.setErrorMessage("服务器工作目录出错:" + e);
            return false;
        }
        return true;
    }

    private boolean compiler(JudgeResult result, File path) {
        ExecutorUtil.ExecMessage msg = HandlerCompiler(path);
        if (msg.getError() != null) {
            result.setStatus(CE);
            result.setMemoryUsed(0L);
            result.setTimeUsed(0L);
            result.setErrorMessage(msg.getError());
            return false;
        }
        return true;
    }

    private void runSrc(JudgeTask task, JudgeResult result, File path) {
        if (task.getType() == 1) {
            oiJudge(task, result, path);
        } else {
            acmJudge(task, result, path);
        }

    }

    private void acmJudge(JudgeTask task, JudgeResult result, File path) {
        String cmd = "script process timeLimit memoryLimit inputFile tmpFile";
        cmd = cmd.replace("script", script);
        cmd = cmd.replace("process", getRunCommand(path).replace(" ", "@"));
        cmd = cmd.replace("timeLimit", task.getTimeLimit().toString());
        cmd = cmd.replace("memoryLimit", task.getMemoryLimit().toString());
        cmd = cmd.replace("tmpFile", path.getPath() + File.separator + "tmp.out");
        int status = AC;
        result.setScore(100);
        result.setGotScore(0);
        long timeUsed = 0L;
        long memoryUsed = 0L;
        String msg = "";
        for (int i = 1; i <= task.getInput().size(); i++) {
            File inFile = new File(path.getPath() + File.separator + i + ".in");
            File outFile = new File(path.getPath() + File.separator + i + ".out");
            if (!inFile.exists() || !outFile.exists()) break;
            ExecutorUtil.ExecMessage remsg = ExecutorUtil.exec(cmd.replace("inputFile", inFile.getPath()), 10000);
            JudgeResultCase caseOne = JSON.parseObject(remsg.getStdout(), JudgeResultCase.class);
            if (caseOne == null) {
                status = SE;
                timeUsed = 0L;
                memoryUsed = 0L;
                break;
            }
            if (remsg.getError() != null) {
                status = RE;
                timeUsed = 0L;
                memoryUsed = 0L;
                msg = remsg.getError();
                break;
            }
            if (caseOne.getStatus() == AC) {
                if (i > 1) {
                    memoryUsed = (memoryUsed + caseOne.getMemoryUsed()) / 2;
                    timeUsed = (timeUsed + caseOne.getTimeUsed()) / 2;
                } else {
                    memoryUsed = caseOne.getMemoryUsed();
                    timeUsed = caseOne.getTimeUsed();
                }
                status = diff(new File(path.getPath() + File.separator + "tmp.out"), outFile);
                if (status != AC) {
                    break;
                }
                if (caseOne.getMemoryUsed() > task.getMemoryLimit()) {
                    status = MLE;
                    break;
                }
                if (caseOne.getTimeUsed() > task.getTimeLimit()) {
                    status = TLE;
                    break;
                }
            } else {
                status = caseOne.getStatus();
                if (i > 1) {
                    memoryUsed = (memoryUsed + caseOne.getMemoryUsed()) / 2;
                    timeUsed = (timeUsed + caseOne.getTimeUsed()) / 2;
                } else {
                    memoryUsed = caseOne.getMemoryUsed();
                    timeUsed = caseOne.getTimeUsed();
                }
            }
            ExecutorUtil.exec("rm " + path.getPath() + File.separator + "tmp.out", 1000);
        }
        result.setErrorMessage(msg);
        result.setMemoryUsed(memoryUsed);
        result.setTimeUsed(timeUsed);
        if (status == PE)
            result.setGotScore(90);
        if (status == AC)
            result.setGotScore(100);
        result.setStatus(status);
    }

    private void oiJudge(JudgeTask task, JudgeResult result, File path) {
        String cmd = StrUtil.format("{} {} {} {} inputFile {}", script, getRunCommand(path).replace(" ", "@"),
                task.getTimeLimit().toString(), task.getMemoryLimit().toString(), path.getPath() + File.separator + "tmp.out");
        int gotScore = 0;
        int score = 0;
        result.setStatus(11);
        for (int i = 1; i <= task.getInput().size(); i++) {
            score += task.getScore().get(i - 1);
            File inFile = new File(path.getPath() + File.separator + i + ".in");
            File outFile = new File(path.getPath() + File.separator + i + ".out");
            if (!inFile.exists() || !outFile.exists()) break;
            ExecutorUtil.ExecMessage remsg = ExecutorUtil.exec(cmd.replace("inputFile", inFile.getPath()), 50000);
            JudgeResultCase caseOne = JSON.parseObject(remsg.getStdout(), JudgeResultCase.class);
            if (caseOne != null && remsg.getError() == null && caseOne.getStatus() == AC) {
                if (diff(new File(path.getPath() + File.separator + "tmp.out"), outFile) == AC) {
                    if (caseOne.getMemoryUsed() <= task.getMemoryLimit() && caseOne.getTimeUsed() <= task.getTimeLimit()) {
                        gotScore += task.getScore().get(i - 1);
                    }
                }
            }
            ExecutorUtil.exec("rm " + path.getPath() + File.separator + "tmp.out", 1000);
        }
        result.setMemoryUsed(0L);
        result.setTimeUsed(0L);
        result.setGotScore(gotScore);
        result.setScore(score);
        result.setType(1);
        if (score == gotScore)
            result.setStatus(0);
        if (gotScore > 0 && gotScore < score)
            result.setStatus(10);
    }

    public int diff(File tmpOut, File stdOut) {
        String tem = FileUtils.read(tmpOut);
        String std = FileUtils.read(stdOut);
        if (tem.equals(std)) {
            return AC;
        }
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < tem.length(); i++) {
            if (tem.charAt(i) != ' ' && tem.charAt(i) != '\n') {
                sb1.append(tem.charAt(i));
            }
        }
        for (int i = 0; i < std.length(); i++) {
            if (std.charAt(i) != ' ' && std.charAt(i) != '\n') {
                sb2.append(std.charAt(i));
            }
        }
        if (sb1.toString().equals(sb2.toString())) {
            return PE;
        } else {
            if (sb1.length() > sb2.length() * 2)
                return OLE;
            return WA;
        }
    }

    public JudgeResult judge(JudgeTask task) {
        JudgeResult result = new JudgeResult();
        result.setType(task.getType());
        if (!checkTask(task, result)) {
            return result;
        }
        if (!security.checkSecurity(task, result)) {
            return result;
        }
        File path = new File(judgePath + File.separator + IdUtil.simpleUUID());
        if (!createWorkspace(task, result, path)) {
            ExecutorUtil.exec("rm -rf " + path.getPath(), 1000);
            return result;
        }
        if (!compiler(result, path)) {
            ExecutorUtil.exec("rm -rf " + path.getPath(), 1000);
            return result;
        }
        runSrc(task, result, path);
        ExecutorUtil.exec("rm -rf " + path.getPath(), 1000);
        return result;
    }

}

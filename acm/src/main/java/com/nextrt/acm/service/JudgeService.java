package com.nextrt.acm.service;

import com.nextrt.acm.biz.judge.JudgeServerBiz;
import com.nextrt.acm.config.thread.ViThreadPoolManager;
import com.nextrt.core.entity.judge.JudgeServer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class JudgeService {
    private final JudgeServerBiz judgeServerBiz;

    public JudgeService(JudgeServerBiz judgeServerBiz) {
        this.judgeServerBiz = judgeServerBiz;
    }

    //更新判题服务器在线状态
    public void updateHeart(JudgeServer judgeServer) {
        checkHealth();
        JudgeServer exitJS = judgeServerBiz.selectJudgeServerBySN(judgeServer.getSn());
        judgeServer.setStatus(1);
        judgeServer.setLastSend(new Date());
        if(exitJS == null){
            judgeServerBiz.addJudgeServer(judgeServer);
        }else {
            judgeServer.setId(exitJS.getId());
            judgeServer.setVersion(exitJS.getVersion());
            judgeServerBiz.updateJudgeServer(judgeServer);
        }
    }

    public void checkHealth()
    {
        ViThreadPoolManager.getInstance().execute(() -> {
            List<JudgeServer> list = judgeServerBiz.getJudgeServerList();
            list.forEach(x->{
                if(x.getLastSend().getTime() + 120*1000 < System.currentTimeMillis()){
                    x.setStatus(-1);
                    judgeServerBiz.updateJudgeServer(x);
                }
            });
        });
    }

    public List<JudgeServer> getJudgeServerList() {
        checkHealth();
        return judgeServerBiz.getJudgeServerList();
    }

    public JudgeServer getJudgeServerByID(int id) {
        return judgeServerBiz.getJudgeById(id);
    }
}

package com.nextrt.acm.biz.judge;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nextrt.core.entity.judge.JudgeServer;
import com.nextrt.core.mapper.judge.JudgeServerMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class JudgeServerBiz {
    private final JudgeServerMapper judgeServerMapper;

    public JudgeServerBiz(JudgeServerMapper judgeServerMapper) {
        this.judgeServerMapper = judgeServerMapper;
    }

    public List<JudgeServer> getJudgeServerList(){
        QueryWrapper<JudgeServer> queryWrapper = new QueryWrapper<>();
        return judgeServerMapper.selectList(queryWrapper);
    }

    public JudgeServer getJudgeById(int id){
        return judgeServerMapper.selectById(id);
    }

    @Async
    public void addJudgeServer(JudgeServer judgeServer){
        judgeServerMapper.insert(judgeServer);
    }

    @Async
    public void updateJudgeServer(JudgeServer judgeServer){
        judgeServerMapper.updateById(judgeServer);
    }

    public JudgeServer selectJudgeServerBySN(String sn){
        QueryWrapper<JudgeServer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(JudgeServer::getSn,sn);
        return judgeServerMapper.selectOne(queryWrapper);
    }
}

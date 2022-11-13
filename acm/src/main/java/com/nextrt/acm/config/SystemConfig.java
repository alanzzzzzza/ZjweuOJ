package com.nextrt.acm.config;

import com.nextrt.acm.biz.system.ConfigBiz;
import org.springframework.stereotype.Component;

@Component
public class SystemConfig {
    public static final String CJudgeSub = "judge-submit";
    public static final String CJudgeResult = "judge-result";
    public static final String CJudgeTask = "judge-task";
    public static final String CJudgeHeart = "judge-heart";

    private final ConfigBiz config;

    public SystemConfig(ConfigBiz config) {
        this.config = config;
    }

    public Integer getInt(String name){
        return config.getInt(name);
    }
    public String getString(String name){
        return config.getString(name);
    }
}

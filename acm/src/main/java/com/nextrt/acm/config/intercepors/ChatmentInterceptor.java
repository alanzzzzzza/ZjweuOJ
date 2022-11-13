package com.nextrt.acm.config.intercepors;

import com.alibaba.fastjson.JSON;
import com.nextrt.acm.controller.admin.AdminSystemController;
import com.nextrt.core.entity.common.SysConfig;
import com.nextrt.core.vo.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class ChatmentInterceptor implements HandlerInterceptor {

    @Resource
    private AdminSystemController adminSystemController;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        List< SysConfig > data = (List<SysConfig>)(adminSystemController.getConfigs().getData());
        boolean isValid = false;
        for(var item : data) {
            if("ChatmentSwitch".equals(item.getName())) {
                if("1".equals(item.getValue())) {
                    isValid = true;
                }
                break;
            }
        }
        if(!isValid) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            try {
                response.getWriter().write(JSON.toJSONString(new Result(403,"讨论区未开启")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isValid;
    }
}

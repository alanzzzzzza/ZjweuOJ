package com.nextrt.acm.config.intercepors;

import com.alibaba.fastjson.JSON;
import com.nextrt.core.vo.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getIntHeader("userId") > 0){
            return true;
        }else{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            try {
                response.getWriter().write(JSON.toJSONString(new Result(-12000,"用户未登陆，请登陆后再试")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
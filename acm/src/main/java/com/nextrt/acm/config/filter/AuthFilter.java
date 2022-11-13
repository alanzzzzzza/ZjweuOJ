package com.nextrt.acm.config.filter;

import com.nextrt.core.vo.Token;
import com.nextrt.acm.util.PasswordEncrypt;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter(filterName = "Auth",urlPatterns = "/**")
public class AuthFilter implements Filter {
    private final PasswordEncrypt encrypt;

    public AuthFilter(PasswordEncrypt encrypt) {
        this.encrypt = encrypt;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Token token = encrypt.checkToken(req.getHeader("Authorization"));
        int userId = token==null?-1:token.getUserId();
        int userLevel = token==null?0:token.getUserLevel();
        int contestId = token==null?0:token.getOther();
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(req) {
            @Override
            public String getHeader(String name) {
                String superHeader = super.getHeader(name);
                if("userId".equals(name)){
                    return String.valueOf(userId);
                }
                if("userLevel".equals(name)){
                    return String.valueOf(userLevel);
                }
                if("contestId".equals(name)){
                    return String.valueOf(contestId);
                }
                return superHeader;
            }
            @Override
            public int getIntHeader(String name) {
                int superHeader =  super.getIntHeader(name);
                if("userId".equals(name)){
                    return userId;
                }
                if("userLevel".equals(name)){
                    return userLevel;
                }
                if("contestId".equals(name)){
                    return contestId;
                }
                return superHeader;
            }
        };
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Server","GeekServer");
        resp.setHeader("Pragma","no-cache");
        resp.setHeader("Cache-Control","no-cache");
        resp.setHeader("Expires","-1");
        chain.doFilter(requestWrapper,resp);
    }
    @Override
    public void destroy() {
    }
}

package com.nextrt.acm.config;

import com.nextrt.acm.config.intercepors.AuthInterceptor;
import com.nextrt.acm.config.intercepors.ChatmentInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private ChatmentInterceptor chatmentInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        this.chatmentInterceptor(registry);
    }

    private void chatmentInterceptor(InterceptorRegistry registry) {
        registry.addInterceptor(chatmentInterceptor)
                .addPathPatterns("/user/chatment/**");
    }
}


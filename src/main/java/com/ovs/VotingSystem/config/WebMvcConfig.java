package com.ovs.VotingSystem.config;

import com.ovs.VotingSystem.interceptor.GlobalVariableInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final GlobalVariableInterceptor globalVariableInterceptor;

    @Autowired
    public WebMvcConfig(GlobalVariableInterceptor globalVariableInterceptor) {
        this.globalVariableInterceptor = globalVariableInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalVariableInterceptor);
    }


}

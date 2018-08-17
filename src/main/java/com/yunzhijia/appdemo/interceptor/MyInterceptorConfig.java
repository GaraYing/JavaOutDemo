package com.yunzhijia.appdemo.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: 配置拦截器
 * @author: GaraYing
 * @create: 2018-08-17 11:03
 **/

@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ContextInterceptor()).addPathPatterns("/app/request/**");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {

    }
}

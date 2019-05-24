package com.spring.aop.config;

import com.spring.aop.interceptor.SpringLearnInterceptor1;
import com.spring.aop.interceptor.SpringLearnInterceptor2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置 WebMvcConfig 添加拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SpringLearnInterceptor1()).addPathPatterns("/**");
        registry.addInterceptor(new SpringLearnInterceptor2()).addPathPatterns("/**");
    }

}

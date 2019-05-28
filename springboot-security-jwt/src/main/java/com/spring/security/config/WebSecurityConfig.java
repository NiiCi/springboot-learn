package com.spring.security.config;

import com.spring.security.authentication.AuthenticationFailureHandlerImpl;
import com.spring.security.authentication.AuthenticationSuccessHandlerImpl;
import com.spring.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author niici
 * spring security　配置类
 */
@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 注入 Security 属性类配置
     */
    @Autowired
    private SecurityProperties securityProperties;

    // 重写PasswordEncoder接口中的方法,实例化加密策略
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 注入自定义的 登录成功或者失败的处理器
     */
    @Autowired
    public AuthenticationSuccessHandlerImpl authenticationSuccessHandler;

    @Autowired
    public AuthenticationFailureHandlerImpl authenticationFailureHandler;


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        String redirectUrl = securityProperties.getBrowser().getLoginPage();
        // 表单登录方式
        httpSecurity.formLogin()
                //登录页面路径
                .loginPage("/authentication/require")
                // 登录表单提交的url
                .loginProcessingUrl("/authentication/form")
                // 添加登录成功/失败 处理器
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                //请求授权
                .authorizeRequests()
                //不需要权限认证的url
                .antMatchers("/authentication/require",redirectUrl).permitAll()
                //任何请求
                .anyRequest()
                //需要身份认证
                .authenticated()
                .and()
                //关闭跨站请求保护
                .csrf().disable();
    }
}

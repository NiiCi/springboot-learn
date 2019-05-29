package com.spring.security.config;

import com.alibaba.fastjson.JSONObject;
import com.spring.security.authentication.JWTAuthenticationFailureHandler;
import com.spring.security.authentication.JWTAuthenticationSuccessHandler;
import com.spring.security.filter.ValidateCodeFilter;
import com.spring.security.properties.SecurityProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author niici
 * spring security　配置类
 */
@Log4j2
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
    public JWTAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    public JWTAuthenticationFailureHandler authenticationFailureHandler;


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        log.info(JSONObject.toJSONString(authenticationSuccessHandler));
        log.info(JSONObject.toJSONString(authenticationFailureHandler));
        /**
         * 创建 验证码 过滤器 ，并将该过滤器的Handler 设置成自定义登录失败处理器
         */
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setFailureHandler(authenticationFailureHandler);
        //将 securityproperties 设置进去
        validateCodeFilter.setSecurityProperties(securityProperties);
        //调用 装配 需要图片验证码的 url 的初始化方法
        validateCodeFilter.afterPropertiesSet();

        String redirectUrl = securityProperties.getBrowser().getLoginPage();
        // 表单登录方式
        httpSecurity
                //在UsernamePasswordAuthenticationFilter 过滤器前 加一个过滤器 来搞验证码
                //.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
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
                .antMatchers("/authentication/require").permitAll()
                .antMatchers(redirectUrl).permitAll()
                .antMatchers("/code/image").permitAll()
                //任何请求
                .anyRequest()
                //需要身份认证
                .authenticated()
                .and()
                //关闭跨站请求保护
                .csrf().disable();
    }
}

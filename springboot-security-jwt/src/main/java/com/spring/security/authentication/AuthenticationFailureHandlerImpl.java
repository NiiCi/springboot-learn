package com.spring.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.properties.LoginType;
import com.spring.security.properties.SecurityProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登录失败处理器，如果认证失败会跳到这个类上来处理
 * 实现 AuthenticationFailureHandler 接口
 * 这里 继承了 AuthenticationFailureHandler 接口下的一个 实现类 SimpleUrlAuthenticationFailureHandler
 */
@Log4j2
@Component
public class  AuthenticationFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityProperties securityProperties;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.info("---登录失败---");
        // 如果是json格式
        if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())){
            // 设置相应码 500
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            // 将 登录失败信息打包成json格式返回
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(e));
        }else{
            super.onAuthenticationFailure(request,response,e);
        }
    }
}

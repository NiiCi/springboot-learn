package com.spring.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.properties.LoginType;
import com.spring.security.properties.SecurityProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登录成功处理器，如果认证成功会跳到这个类上来处理
 * 实现 AuthenticationSuccessHandler 接口
 * 这里 继承了 AuthenticationSuccessHandler 接口下的一个实现类 SavedRequestAwareAuthenticationSuccessHandler
 * @author niici
 */
@Log4j2
@Component
public class JWTAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityProperties securityProperties;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("---登录成功---");

        // 判断 是 json 格式返回还是 view 格式返回
        // securityProperties 中 默认了 loginType 为 view
        if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())){
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(authentication));
        }else {
            // 返回 view
            super.onAuthenticationSuccess(request,response,authentication);
        }
    }
}

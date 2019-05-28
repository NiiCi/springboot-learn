package com.spring.security.filter;


import com.spring.security.bean.ImageCode;
import com.spring.security.config.ImageConstants;
import com.spring.security.exception.ValidateCodeException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

/**
 * 图形验证码过滤器
 */
@Log4j2
public class ImageCodeFilter extends OncePerRequestFilter {
    @Getter
    @Setter
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Getter
    @Setter
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 判断请求是否为登录,并且请求方式为post
        if (StringUtils.equals("/auth/login", request.getRequestURI())
                && StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
            try {
                validate(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                log.error(e.getMessage(),e);
                throw new ValidateCodeException(e.getMessage());
            }
        }
        filterChain.doFilter(request,response);
    }

    /**
     * 校验验证码
     * @param request
     * @throws ServletRequestBindingException
     */
    private void validate(ServletWebRequest request) throws ServletRequestBindingException{

        // 从 session 中 获取 验证码
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request, ImageConstants.SESSION_KEY_CODE);
        log.info(" session 中的验证码: {}",codeInSession.getCode());
        // 从请求中获取用户提交的验证码
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), ImageConstants.LOGIN_FORM_CODE);
        log.info(" 用户提交 的验证码: {}",codeInRequest);
        if (codeInSession == null){
            throw new ValidateCodeException("验证码不存在");
        }
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空");
        }
        if (codeInSession.isExpire()){
            // 验证码过期,从session中删除key
            sessionStrategy.removeAttribute(request,ImageConstants.SESSION_KEY_CODE);
            throw new ValidateCodeException("验证码过期");
        }
        if(!StringUtils.equals(codeInRequest,codeInSession.getCode())){
            throw new ValidateCodeException("验证码不匹配");
        }
        // 如果校验通过 删除验证码
        sessionStrategy.removeAttribute(request,ImageConstants.SESSION_KEY_CODE);
    }
}

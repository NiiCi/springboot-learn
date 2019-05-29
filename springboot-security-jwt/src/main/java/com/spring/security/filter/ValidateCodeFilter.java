package com.spring.security.filter;

import com.spring.security.authentication.JWTAuthenticationFailureHandler;
import com.spring.security.bean.ImageCode;
import com.spring.security.controller.ValidateCodeController;
import com.spring.security.exception.ValidateCodeException;
import com.spring.security.properties.SecurityProperties;
import com.spring.security.properties.ValidateCodeProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 4.验证码过滤器
 * 在用户密码验证之前进行验证码验证
 */
@Log4j2
@Component
@Data
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    /**
     * 注入 登录失败 处理器
     */
    @Autowired
    private JWTAuthenticationFailureHandler failureHandler;

    /**
     * Session 对象
     * 验证码我们在生成时 添加到了session 中
     */
    @Getter
    @Setter
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 创建一个Set 集合 存放 需要验证码的 urls
     */
    private Set<String> urls = new HashSet<>();

    /**
     * 注入 security 属性配置类
     */
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * spring的一个工具类:用来判断 两个字符串 是否匹配
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 这个方法是 InitializingBean 接口下的一个方法， 在初始化配置完成后 运行此方法
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        //在初始化配置完成后 运行此方法
        super.afterPropertiesSet();
        // 读取验证码的配置属性
        ValidateCodeProperties code = securityProperties.getCode();
        logger.info(String.valueOf(code));
        //将 application 配置中的 url 属性进行 切割
        String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getImage().getUrl(), ",");
        //添加到 Set 集合里
        urls.addAll(Arrays.asList(configUrls));
        //因为登录请求一定要有验证码 ，所以直接 add 到set 集合中
        urls.add("/authentication/form");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean action = false;
        for (String url:urls){
            //如果请求的url 和 配置中的url 相匹配
            if (pathMatcher.match(url,request.getRequestURI())){
                action = true;
            }
        }

        //拦截请求
        if (action){
            logger.info("拦截成功"+request.getRequestURI());
            //如果是登录请求
            try {
                validate(new ServletWebRequest(request));
            }catch (ValidateCodeException exception){
                //返回错误信息给 失败处理器
                failureHandler.onAuthenticationFailure(request,response,exception);
            }
        }else {
            //不做任何处理，调用后面的 过滤器
            filterChain.doFilter(request,response);
        }
    }

    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        //从session中取出 验证码
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request, ValidateCodeController.SESSION_KEY);
        //从request 请求中 取出 验证码
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),"imageCode");

        if (StringUtils.isBlank(codeInRequest)){
            logger.info("验证码不能为空");
            throw new ValidateCodeException("验证码不能为空");
        }
        if (codeInSession == null){
            logger.info("验证码不存在");
            throw new ValidateCodeException("验证码不存在");
        }
        if (codeInSession.isExpired()){
            logger.info("验证码已过期");
            sessionStrategy.removeAttribute(request,ValidateCodeController.SESSION_KEY);
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StringUtils.equals(codeInSession.getCode(),codeInRequest)){
            logger.info("验证码不匹配"+"codeInSession:"+codeInSession.getCode() +", codeInRequest:"+codeInRequest);
            throw new ValidateCodeException("验证码不匹配");
        }
        //把对应 的 session信息  删掉
        sessionStrategy.removeAttribute(request,ValidateCodeController.SESSION_KEY);
    }
}

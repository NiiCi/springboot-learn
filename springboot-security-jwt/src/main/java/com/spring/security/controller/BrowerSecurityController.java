package com.spring.security.controller;


import com.spring.security.base.BaseResult;
import com.spring.security.properties.SecurityProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 判断 json/html 请求,返回不同的结果
 * 响应码 401 , UNAUTHORIZED
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
@Controller
@Log4j2
public class BrowerSecurityController {

    // 重定向策略
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    // 把当前请求缓存到 session 中
    private RequestCache requestCache = new HttpSessionRequestCache();

    /**
     * 注入 securityProperties 属性类 配置
     */
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 需要身份认证时,跳转到这里
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/authentication/require")
    @ResponseBody
    public Object requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 拿到请求对象
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            // 获取 跳转url
            String targetUrl = savedRequest.getRedirectUrl();
            log.info("引发跳转的请求是 , {}", targetUrl);

            // 判断 targerUrl 是否为 .html 结尾,如果是 跳转到登录页(返回view)
            if (StringUtils.endsWithIgnoreCase(targetUrl,".html")) {
                String redirectUrl = securityProperties.getBrowser().getLoginPage();
                log.info("重定向路径 ---- {}",redirectUrl);
                redirectStrategy.sendRedirect(request,response,redirectUrl);
            }
        }
        // 如果 targetUrl 不是 .html 说明是 json 请求,返回json字符串提示信息
        return new BaseResult("访问的服务需要认证,请引导用户到登录页");
    }

    @RequestMapping("/login.html")
    public Object loginHtml(HttpServletRequest request, HttpServletResponse response) throws IOException {
       return "login";
    }
}

package com.spring.aop.filter;

import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * filter全局拦截的配置("/*") 和 拦截器的全局拦截配置("/**") 有所区别
 * 通过@WebFilter("/*")注解，首先需要在启动类上加@ServletComponentScan("com.spring.aop.filter")
 * @author niici
 */
@WebFilter("/*")
@Log4j2
public class SpringLearnFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("filter init ------");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("filter doFilter ------");
        // 条件满足的情况下,放行进行后续请求操作
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        log.info("filter destroy ------");
    }
}

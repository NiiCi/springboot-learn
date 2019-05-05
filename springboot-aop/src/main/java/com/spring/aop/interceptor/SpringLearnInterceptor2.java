package com.spring.aop.interceptor;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 实现 HanderInterceptor 实现 拦截器
 * @author niici
 */
@Log4j2
public class SpringLearnInterceptor2 implements HandlerInterceptor {
    /**
     * 请求过来之后首先走的方法  return true 之后继续往下执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime",System.currentTimeMillis());
        log.info("----- springLearnInterceptor2 preHandle -----");
        return true;
    }

    /**
     * 请求之后，返回之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = Long.parseLong(request.getAttribute("startTime").toString());
        log.info("----- springLearnInterceptor2 执行: " + (System.currentTimeMillis() - startTime));
        log.info("----- springLearnInterceptor2 postHandle -----");
    }

    /**
     * 处理完成之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        request.removeAttribute("startTime");
        log.info("----- springLearnInterceptor2 afterCompletion -----");
    }
}

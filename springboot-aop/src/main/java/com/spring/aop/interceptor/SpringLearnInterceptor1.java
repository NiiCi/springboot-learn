package com.spring.aop.interceptor;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 继承 HanderInterceptorAdapter 实现 拦截器
 * @author niici
 */
@Log4j2
public class SpringLearnInterceptor1 extends HandlerInterceptorAdapter {
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
        log.info("----- springLearnInterceptor1 preHandle -----");
        return super.preHandle(request, response, handler);
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
        log.info("----- springLearnInterceptor1 执行: " + (System.currentTimeMillis() - startTime));
        log.info("----- springLearnInterceptor1 postHandle -----");
        super.postHandle(request, response, handler, modelAndView);
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
        log.info("----- springLearnInterceptor1 afterCompletion -----");
        super.afterCompletion(request, response, handler, ex);
    }

    /**
     * 如果返回一个 current 类型的变量,会启用一个新的线程
     * 执行完 preHandle 方法之后立即会调用 afterConcurrentHandlingStarted,然后在新线程再次执行 preHandle postHandle afterCompletion
     * @param request
     * @param response
     * @param handler
     * @throws Exception
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("----- springLearnInterceptor1 afterConcurrentHandlingStarted -----");
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}

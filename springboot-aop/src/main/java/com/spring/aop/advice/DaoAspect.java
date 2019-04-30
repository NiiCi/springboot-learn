package com.spring.aop.advice;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

@Component //添加 组件注解
@Aspect // 声明是一个 切面类
@Log4j2
public class DaoAspect {
    @Pointcut("execution(* com.spring.aop.service.*.* (..))")
    public void executeDao() {
    }

    /**
     * JoinPoint 可以获取通知的签名信息，如目标方法名，目标方法参数信息等
     * RequestContextHolder 可以获取请求信息，session 信息
     * @param joinPoint
     */
    @Before("executeDao()")
    public void beforeDaoDo(JoinPoint joinPoint){
        log.info("spring aop aspectj 开启 -----");
        //获取 request 请求信息
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        log.info(request.getParameter("username"));
    }
}

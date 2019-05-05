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
import java.lang.reflect.Modifier;

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
        log.info("从 joinPoint 中获取 request 并获取请求参数 ----- "+request.getParameter("username"));
        // 目标方法名
        log.info("目标方法名 ----- "+joinPoint.getSignature().getName());
        // 目标方法声明类型
        log.info("目标方法声明类型 ----- "+Modifier.toString(joinPoint.getSignature().getModifiers()));
        // 目标方法所属类的类名
        log.info("目标方法所属类的类名 ----- "+joinPoint.getSignature().getDeclaringTypeName());
        // 目标方法所属类的简单类名
        log.info("目标方法所属类的简单类名 ----- "+joinPoint.getSignature().getDeclaringType().getSimpleName());
        // 传入目标方法的参数
        log.info("传入目标方法的参数个数 ----- "+joinPoint.getArgs().length);
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            log.info("第 "+ (i+1)+" 个参数的参数值为"+" "+joinPoint.getArgs()[i]);
        }
    }
}

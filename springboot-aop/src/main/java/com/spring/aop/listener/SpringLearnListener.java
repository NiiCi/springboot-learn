package com.spring.aop.listener;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 * 实现 ServletRequestListener 接口 自定义监听器
 * 也需要 @ServletComponentScan 指定扫描包
 * ServletContextListener 在项目启动时就加载, 只加载一次
 * ServletRequestListener 在有请求进来时加载，请求一次 加载一次
 */
@WebListener("/*")
@Log4j2
public class SpringLearnListener implements ServletContextListener,ServletRequestListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info(" listener contextInitialized ------ ");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }


    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        log.info(" listener requestInitialized ------ ");
    }
}

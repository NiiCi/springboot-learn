package com.spring.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan({"com.spring.aop.filter","com.spring.aop.listener"}) //开启 监听器 和 过滤器包扫描
public class AspectJApplication {
    public static void main(String[] args) {
        SpringApplication.run(AspectJApplication.class,args);
    }
}

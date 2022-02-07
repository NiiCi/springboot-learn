package com.niici;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author niici
 */
@SpringBootApplication
@Slf4j
public class DemoApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DemoApplication.class, args);
        // 获取springboot容器中的bean
        for (String beanDefinitionName : run.getBeanDefinitionNames()) {
            log.info(beanDefinitionName);
        }
        //run.getBean("person").getClass().getName();
    }
}

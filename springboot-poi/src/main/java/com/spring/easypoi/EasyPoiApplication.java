package com.spring.easypoi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.spring.easypoi.dao")
public class EasyPoiApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyPoiApplication.class,args);
    }
}

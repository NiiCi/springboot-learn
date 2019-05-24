package com.niici.jsoup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.niici.jsoup.dao")
public class JsoupApplication {
    public static void main(String[] args) {
        SpringApplication.run(JsoupApplication.class,args);
    }
}

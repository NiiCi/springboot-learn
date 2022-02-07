package com.niici.config;

import com.niici.bean.Person;
import com.niici.bean.Pet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //  告诉springboot这是一个配置类 == 配置文件
public class DemoConfig {

    @Bean("person") //给容器中添加bean组件，默认以方法名作为bean的id，通过@Bean("person")指定id，返回值为对应的实例。
    public Person initPerson(){
        return new Person("niici", 18);
    }

    @Bean
    public Pet initPet(){
        return new Pet("dog");
    }
}

package com.spring.aop.dao;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

public class UserDao {
    @Before("executeDao()")
    public void sayHello(){
        System.out.println("test cglib proxy -----");
    }
}

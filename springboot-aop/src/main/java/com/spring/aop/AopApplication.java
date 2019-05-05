package com.spring.aop;

import com.spring.aop.aop.ProxyFactory;
import com.spring.aop.dao.UserDao;

public class AopApplication {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        //获取代理对象
        UserDao proxy = (UserDao) new ProxyFactory(userDao).getProxyInstance();
        proxy.sayHello();
    }
}

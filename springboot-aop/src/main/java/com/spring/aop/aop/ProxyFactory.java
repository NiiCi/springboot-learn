package com.spring.aop.aop;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.sql.SQLOutput;

/**
 *  自定义 cglib 动态代理
 *  需要 实现 MethodInterceptor 接口
 */
public class ProxyFactory implements MethodInterceptor {
    /**
     * 被代理的目标对象
     */
    private Object target;

    public ProxyFactory(Object target) {
        this.target = target;
    }

    /**
     * 生成一个 代理对象
     * @return
     */
    public Object getProxyInstance(){
        //1. 工具类
        Enhancer en = new Enhancer();
        //2. 设置父类
        en.setSuperclass(target.getClass());
        //3. 设置回调函数
        en.setCallback(this);
        return en.create();
    }

    /**
     * 代理对象 执行 目标对象中的方法
     * @param proxy
     * @param method
     * @param args
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("开始事务----");
        // 执行目标对象的方法
        Object proxyTarget = methodProxy.invoke(target,args);
        System.out.println("提交事务----");
        return proxyTarget;
    }
}

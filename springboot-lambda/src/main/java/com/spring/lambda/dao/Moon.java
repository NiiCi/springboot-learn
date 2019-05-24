package com.spring.lambda.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * 被观察者 Moon
 * 持有一组 LandingObserver 实例
 * 有东西着陆时会通知这些观察者,还可以添加新的观察者,观测 Moon 对象
 */
public class Moon {
    private List<LandingObserver> observers = new ArrayList<>();

    // 着陆方法,当有东西着陆时,会通知观测者
    public void land(String name){
        for (LandingObserver observer : observers) {
            observer.observeLanding(name);
        }
    }

    // 添加一个观察者,观测 moon
    public void startSpying(LandingObserver observer){
        observers.add(observer);
    }
}

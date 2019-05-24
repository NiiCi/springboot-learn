package com.spring.lambda.dao;

import lombok.extern.log4j.Log4j2;

@Log4j2
/**
 * 观察者接口实现类 ---- Nasa
 */
public class Nasa implements LandingObserver {
    @Override
    public void observeLanding(String name) {
        if (name.contains("human")){
            log.info("我着陆到月球了!!!");
        }
    }
}

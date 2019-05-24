package com.spring.lambda.dao;

import lombok.extern.log4j.Log4j2;

@Log4j2
/**
 * 观察者接口实现类 ---- 外星人
 */
public class Aliens implements LandingObserver {
    @Override
    public void observeLanding(String name) {
        if (name.contains("human")){
            log.info("检测到人类着陆,可以开始入侵地球!!!");
        }
    }
}

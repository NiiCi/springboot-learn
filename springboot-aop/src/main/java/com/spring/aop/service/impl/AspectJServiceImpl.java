package com.spring.aop.service.impl;

import com.spring.aop.service.AspectJService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service("aspectJService")
@Log4j2
public class AspectJServiceImpl implements AspectJService {
    @Override
    public void test() {
        log.info("service 测试 aop");
    }
}

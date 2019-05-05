package com.spring.aop.controller;

import com.spring.aop.service.AspectJService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aop")
public class AspectJController {
    @Autowired
    private AspectJService aspectJService;

    @PostMapping
    public void testAop(@RequestParam("username")String username){
        aspectJService.test(username);
    }
}

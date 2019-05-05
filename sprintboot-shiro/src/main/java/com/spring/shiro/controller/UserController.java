package com.spring.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
    public ResponseEntity<Object> userLogin(@RequestParam("username") String username,@RequestParam("password") String password){
        // 获取当前对象,会自动绑定到当前线程
        Subject subject = SecurityUtils.getSubject();
        // 将用户名和密码 放入 token 中
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        // 执行 shiro 登录验证
        subject.login(token);
        return ResponseEntity.ok().build();
    }
}

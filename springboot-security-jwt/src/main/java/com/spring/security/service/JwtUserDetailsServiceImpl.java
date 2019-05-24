package com.spring.security.service;

import com.spring.security.bean.JWTUserFactory;
import com.spring.security.bean.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

/**
 *第二个要实现的是 UserDetailsService，
 * 这个接口只定义了一个方法 loadUserByUsername，
 * 顾名思义，就是提供一种从用户名可以查到用户并返回的方法。
 * 注意，不一定是数据库哦，文本文件、xml文件等等都可能成为数据源，
 * 这也是为什么Spring提供这样一个接口的原因：保证你可以采用灵活的数据源。
 * 接下来我们建立一个 JwtUserDetailsServiceImpl 来实现这个接口。
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    /**
     * 通过用户名获取用户
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 模拟一个用户,实际应该是从数据库中读取
        User user = new User("1","niici","asdasd","1125382627@qq.com",new Date(), Arrays.asList("user","admin"));
        if (user == null){
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }else{
            return JWTUserFactory.create(user);
        }
    }
}

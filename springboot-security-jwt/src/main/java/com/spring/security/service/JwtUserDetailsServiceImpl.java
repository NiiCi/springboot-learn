package com.spring.security.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * UserDetail 类
 * 加载用户数据,返回UserDetail 实例(里面包含用户信息)
 */
@Service
@Log4j2
public class JwtUserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 通过用户名获取用户
     * Spring Security接收login请求调用UserDetailsService这个接口中的loadUserByUsername方法
     * loadUserByUsername根据传进来的用户名进行校验工作，最后将查询到的用户信息封装到UserDetails这个接口的实现类中
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 模拟一个用户,实际应该是从数据库中读取
        log.info("用户登录 : {}",username);
        String password = passwordEncoder.encode("asdasd");
        // UserDetail 有三个参数 (用户名+密码+权限)
        //根据查找到的用户信息判断用户是否被冻结
        log.info("数据库中的用户密码 : {}",password);
        /**
         * enabled 代表是否可用
         * accountNonExpired 代表 账号是否过期
         * credentialsNonExpired 代表 授权是否过期
         * accountNonLocked 代表 账号是否被锁定
         */
        return new User(username,password,true,true,true,true,AuthorityUtils.createAuthorityList("user","admin"));
    }
}

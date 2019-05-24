package com.spring.security.bean;

import com.spring.security.config.JwtUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public final class JWTUserFactory {
    private JWTUserFactory(){

    }

    public static JwtUser create(User user){
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                mapToGrantedAuthorities(user.getRoles()),
                user.getLastPasswordResetDate()
        );
    }

    public static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities){
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}

package com.spring.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义验证码校验异常
 */
public class ValidateCodeException extends AuthenticationException {
    public ValidateCodeException(String msg){
        super(msg);
    }
}

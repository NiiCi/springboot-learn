package com.spring.security.dao;

import com.spring.security.bean.ImageCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 1.验证码生成器接口
 * 使用接口定义的原因是 方便拓展,如以后需要写一个 英文和数字混合的验证码等
 */
public interface ValidateCodeGenerator {
    /**
     * 创建验证码
     */
    ImageCode createCode(ServletWebRequest request);
}

package com.spring.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Security 属性类
 */
// 获取配置属性前缀
@ConfigurationProperties(prefix = "niici.security")
@Component
@Data
public class SecurityProperties {
    /**
     * 浏览器 属性类
     */
    private BrowserProperties browser = new BrowserProperties();

    /**
     * 验证码 属性类
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();
}

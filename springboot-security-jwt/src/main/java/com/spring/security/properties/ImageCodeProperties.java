package com.spring.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix="niici.security.code")
public class ImageCodeProperties {
    /**
     * 验证码宽度
     */
    private int width  = 67;
    /**
     * 高度
     */
    private int height = 23;
    /**
     * 长度（几个数字）
     */
    private int length  = 4;
    /**
     * 过期时间
     */
    private int expireIn = 60;
    /**
     * 需要图形验证码的 url
     */
    private String url;

    // private Set<String> url;
}

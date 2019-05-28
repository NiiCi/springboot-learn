package com.spring.security.config;

/**
 * 图形验证码常量配置接口
 * 在接口中声明的变量为常量
 */
public interface ImageConstants {
    /**
     * 验证码宽度
     */
    int CODE_WIDTH = 67;

    /**
     * 验证码高度
     */
    int CODE_HEIGHT = 23;

    /**
     * 验证码的过期时间
     */
    int CODE_EXPIREIN = 60;

    /**
     * 验证码的位数
     */
    int CODE_LENGTH = 4;

    /**
     * 验证码字体大小
     */
    int CODE_FONT_SIZE = 20;

    /**
     * 验证码图片格式
     */
    String CODE_TYPE = "JPEG";

    /**
     * Session中存验证码的key值
     */
    String SESSION_KEY_CODE = "SESSION_KEY_IMAGE_CODE";

    /**
     * 登录表单验证码的 name 值
     */
    String LOGIN_FORM_CODE = "code";
}

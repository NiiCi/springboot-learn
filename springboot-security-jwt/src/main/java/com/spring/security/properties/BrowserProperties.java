package com.spring.security.properties;

import lombok.Data;

/**
 * browser(浏览器)配置文件里的： niici.security.browser.loginPage 属性类
 */
@Data
public class BrowserProperties {
    /**
     * loginPage 默认值 是 /browser-login.html
     * 如果 application.yml 中有对 loginPage 的声明获取 yml 中的值
     */
    private String loginPage = "/browserLogin";

    /**
     * 默认返回 JSON 类型
     */
    private LoginType loginType = LoginType.JSON;
}

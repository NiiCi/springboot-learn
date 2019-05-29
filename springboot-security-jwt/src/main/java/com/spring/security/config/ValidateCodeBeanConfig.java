/*
package com.spring.security.config;

import com.spring.security.dao.ValidateCodeGenerator;
import com.spring.security.properties.SecurityProperties;
import com.spring.security.util.ImageCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

*/
/**
 * 注入ImageCodeGenerator到容器
 *//*

@Configuration
public class ValidateCodeBeanConfig {
    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    */
/**
     *
     *
     *
     * 在触发 ValidateCodeGenerator 之前会检测有没有imageCodeGenerator这个bean。
     * 如果有则不作操作,反之,则创建一个 ImageCodeGenerator 实例
     *
     * 为什么要这样呢？如果我们以后重写了一个更牛b的生成验证码类，
     * 我们可以直接给它上面添加@Component注解来注入，
     * 就不用来管原来的代码，也不用考虑bean 名称的冲突。
     *//*

    public ValidateCodeGenerator imageCodeGenerator(){
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setSecurityProperties(securityProperties);
        return codeGenerator;
    }
}
*/

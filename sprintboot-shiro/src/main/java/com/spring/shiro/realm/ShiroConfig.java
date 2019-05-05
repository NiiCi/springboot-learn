package com.spring.shiro.realm;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;

import java.util.LinkedHashMap;
import java.util.Map;


public class ShiroConfig {
    //配置shiroFilter 过滤器
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/logout", "logout");

        filterChainDefinitionMap.put("/userLogin","anon");
        filterChainDefinitionMap.put("/changeLanguage","anon");
        filterChainDefinitionMap.put("/logo.png","anon");
        filterChainDefinitionMap.put("/shiro.css","anon");
        filterChainDefinitionMap.put("/css/*","anon");
        filterChainDefinitionMap.put("/js/*","anon");
        filterChainDefinitionMap.put("/image/*","anon");
        filterChainDefinitionMap.put("/My97DatePicker/*","anon");
        filterChainDefinitionMap.put("/*","user");

		/*filterChainDefinitionMap.put("/admin","roles[admin]");
		filterChainDefinitionMap.put("/user","roles[user]");*/
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setLoginUrl("/");
        shiroFilterFactoryBean.setSuccessUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    //创建jdbcRealm 域 ,进行加密
    //配置自定义 Realm 类
    @Bean
    public ShiroRealm shiroRealm(){
        ShiroRealm shiroRealm = new ShiroRealm();
        HashedCredentialsMatcher credentialsMatcher  = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1000);
        //设置加密
        shiroRealm.setCredentialsMatcher(credentialsMatcher);
        return shiroRealm;
    }

    //配置安全管理器
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager  = new DefaultWebSecurityManager();
        //设置realm
        securityManager.setRealm(shiroRealm());

        return securityManager;
    }

    //配置默认顾问自动代理,由advisor决定对哪些类的方法进行AOP代理
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    //配置shiro 生命周期(可选)
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();

    }

    //配置 shiro 与 spring 关联
    //开启 shiro 注解
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }
}

package com.zhi.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.zhi.shiro.AdminRealm;
import com.zhi.shiro.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * shiro的配置类
 */
@Configuration
public class ShiroConfig {
    /**
     * 创建 ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //1、设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //添加shiro内置过滤器
        /**
         * shiro内置过滤器，可以实现权限相关的拦截器
         * 常用的过滤器
         * anon:无需认证（登录）可以访问
         * authc:必须认证才可以访问
         * user：如果使用rememberMe的功能可以直接访问
         * perms:该资源必须获得资源权限才可以访问
         * role:该资源必须获得角色权限才可以访问
         */
        Map<String, String> filterMap = new LinkedHashMap<String, String>();

        filterMap.put("/index", "anon");
        filterMap.put("/tologin", "anon");
        filterMap.put("/noAuth", "anon");
        filterMap.put("/first", "authc");

        //授权过滤器
        filterMap.put("/user/add", "perms[user_add]");
        filterMap.put("/admin/add", "perms[admin_add]");


        //修改跳转页面
//        shiroFilterFactoryBean.setLoginUrl("/index");
//        //设置未授权提示页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");
        filterMap.put("/*", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    /**
     * creat DefaultWebSecurityManager
     * 安全管理器
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(modularRealmAuthenticator());
        List<Realm> realms = new ArrayList<>();

        realms.add(UserRealm());
        realms.add(AdminRealm());

        //关联realm
        securityManager.setRealms(realms);
        //将用户授权信息注入记住我管理器
        securityManager.setRememberMeManager(rememberMeManager());

        return securityManager;
    }


    /**
     * creat Realm
     */
    @Bean(name = "userRealm")
    public UserRealm UserRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }

    @Bean(name = "adminRealm")
    public AdminRealm AdminRealm() {
        AdminRealm adminRealm = new AdminRealm();
        adminRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return adminRealm;
    }

    /**
     * 配置shiroDialect 用于thymeleaf和shiro标签的配合使用
     */

    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }


    //因为我们的密码是加过密的，所以，如果要Shiro验证用户身份的话，需要告诉它我们用的是md5加密的，并且是加密了两次。同时我们在自己的Realm中也通过SimpleAuthenticationInfo返回了加密时使用的盐。这样Shiro就能顺利的解密密码并验证用户名和密码是否正确了。
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
//        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    /**
     * cookie对象
     *
     * @return 生效时间
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        //设置cookie名称 对应前端的remember
        SimpleCookie simpleCookie = new SimpleCookie("remember");
        simpleCookie.setHttpOnly(true);
        //设置记住我的生效时间 单位为秒
        simpleCookie.setMaxAge(60);
        return simpleCookie;
    }

    /**
     * cookie管理对象
     * rememberMeManager()方法是生成rememberMe管理器，
     * 而且要将这个rememberMe管理器设置到securityManager中
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
//        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }

    @Bean
    public ModularRealmAuthorizer modularRealmAuthorizer() {
        //自己重写的ModularRealmAuthorizer
        return new UserModularRealmAuthorizer();
    }

    @Bean
    public UserModularRealmAuthenticator modularRealmAuthenticator() {
        //自己重写的ModularRealmAuthorizer
        return new UserModularRealmAuthenticator();
    }


}

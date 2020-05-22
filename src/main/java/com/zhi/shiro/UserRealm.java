package com.zhi.shiro;

import com.zhi.entity.AuthUser;
import com.zhi.service.AuthUserService;
import com.zhi.shiro.config.UserModularRealmAuthenticator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(UserRealm.class);
    //注入业务
    @Autowired
    private AuthUserService authUserService;

    /**
     * 执行授权逻辑
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("user执行授权逻辑");

        //给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //添加资源的授权字符串
        //info.addStringPermission("add");

        //获取当前登录用户的信息
        Subject subject = SecurityUtils.getSubject();
        AuthUser user = (AuthUser) subject.getPrincipal();

        //在数据库中查询当前用户的授权字符串
        AuthUser dbUser = authUserService.queryById(user.getUid());
        info.addStringPermission(dbUser.getPerms());

        return info;
    }


    /**
     * 执行认证逻辑
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("user执行认证逻辑");

        //编写shiro判断逻辑，判断用户名和密码
        //判断用户名
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        AuthUser user = authUserService.findByName(token.getUsername());

        if (user == null ||user.getIsAdmin() != 0) {
            return null;
            //y用户名不存在，或者身份匹配不正确 shiro底层返回抛出异常
        }

        //判断密码
        return new SimpleAuthenticationInfo(user, user.getPassword(), "UserRealm");
        //会自动进行对比判断

    }
}

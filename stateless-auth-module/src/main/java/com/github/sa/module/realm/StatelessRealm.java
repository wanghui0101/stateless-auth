package com.github.sa.module.realm;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.sa.core.Account;
import com.github.sa.core.service.SessionService;
import com.github.sa.module.authc.StatelessToken;

/**
 * 认证与鉴权。可用于登录后的所有URL。
 * 
 * @author wh
 * @lastModified 2016-6-7 16:10:06
 */
public class StatelessRealm extends AuthorizingRealm {
    
    @Autowired
    private SessionService sessionService;
    
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessToken; // 通过Token类型与Filter关联, 见com.github.sa.module.web.filter.StatelessAccessAuthcFilter.createToken()
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	StatelessToken statelessToken = (StatelessToken) token;
        String accessToken = statelessToken.getToken();
        Account account = sessionService.retrieve(accessToken); // 从redis中获取当前用户信息
        if (account != null && StringUtils.equals(accessToken, account.getAccessToken())) { // 防止access_token伪造
            return new SimpleAuthenticationInfo(account, statelessToken.getToken(), getName());
        }
        return null;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	Account account = (Account) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(sessionService.getRoles(account.getAccessToken()));
        info.addStringPermissions(sessionService.getPermissions(account.getAccessToken()));
        return info;
    }

}

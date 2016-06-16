package com.github.sa.cas.realm;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.sa.cas.authc.CaptchaUsernamePasswordToken;
import com.github.sa.core.Account;
import com.github.sa.core.service.AccountService;
import com.github.sa.core.service.SessionService;

/**
 * 登录认证
 * 
 * @author Wh
 * @lastModified 2016-1-7 17:51:11
 */
public class StatelessLoginRealm extends AuthenticatingRealm { // 继承AuthenticatingRealm: 只认证, 不鉴权
    
    private static final Logger logger = LoggerFactory.getLogger(StatelessLoginRealm.class);

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private SessionService sessionService;
    
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CaptchaUsernamePasswordToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        logger.debug("token {} start to authenticate", ToStringBuilder.reflectionToString(authcToken));
        CaptchaUsernamePasswordToken token = (CaptchaUsernamePasswordToken) authcToken;
        String username = token.getUsername();
        Account account = accountService.findByAccountName(username); // 从数据库中获取账户信息
        if (account != null) {
            logger.debug("User {} exists", username);
            return new SimpleAuthenticationInfo(account, account.getPassword(), ByteSource.Util.bytes(Hex.decode(account.getSalt())), getName());
        }
        logger.debug("User {} do not exists", username);
        return null;
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(accountService.getHashAlgorithm());
        matcher.setHashIterations(accountService.getHashInterations());
        setCredentialsMatcher(matcher);
    }

}

package com.github.sa.module.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.sa.core.accesstoken.AccessTokenAware;
import com.github.sa.module.authc.StatelessToken;

/**
 * 访问认证过滤器
 * 
 * @author wh
 * @lastModified 2016-6-7 16:06:52
 */
public class StatelessAccessAuthcFilter extends AccessControlFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(StatelessAccessAuthcFilter.class);
    
    @Autowired
    private AccessTokenAware accessTokenAware;
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        logger.debug("start to handle {}", WebUtils.toHttp(request).getRequestURI());
        
        String accessToken = accessTokenAware.getAccessToken(request);
        logger.debug("accessToken: {}", accessToken);
        
        if (StringUtils.isNotEmpty(accessToken)) { // 有访问令牌, 则执行登录认证
            AuthenticationToken token = createToken(accessToken);
            Subject subject = getSubject(request, response);
            try {
                subject.login(token);
                onStatelessAuthcSuccessfully(request, response, accessToken);
            } catch (AuthenticationException e) {
                return onStatelessAuthcFailure(request, response, e);
            }
            return true; // login success, go forward
        }
        return false; // go to onAccessDenied()
    }

    protected void onStatelessAuthcSuccessfully(ServletRequest request, ServletResponse response, String accessToken) {
		logger.debug("{} from IP {} login successfully.", accessToken, request.getRemoteAddr());
	}

	protected Boolean onStatelessAuthcFailure(ServletRequest request, ServletResponse response, AuthenticationException e) {
        logger.info("Stateless principle authenticate fail. Maybe accessToken has timed out.");
        logger.error("{}", e.getMessage());
        return false; // go to onAccessDenied()
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        redirectToLogin(request, response);
        return false;
    }

    protected AuthenticationToken createToken(String accessToken) {
        return new StatelessToken(accessToken);
    }
    
}
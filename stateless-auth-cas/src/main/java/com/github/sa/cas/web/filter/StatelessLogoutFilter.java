package com.github.sa.cas.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.sa.core.accesstoken.AccessTokenAware;
import com.github.sa.core.service.SessionService;

/**
 * 登出过滤器
 * 
 * @author wh
 * @lastModified 2016-6-7 16:24:54
 */
public class StatelessLogoutFilter extends LogoutFilter {

    @Autowired
    private SessionService sessionService;
    
    @Autowired
    private AccessTokenAware accessTokenAware;
    
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response)
            throws Exception {
        clearSession(request, response);
        return super.preHandle(request, response);
    }
    
    protected void clearSession(ServletRequest request, ServletResponse response) {
        String accessToken = accessTokenAware.getAccessToken(request);
        sessionService.invalidate(accessToken);
        Cookie c = new Cookie(accessTokenAware.getAccessTokenKey(), null);
        c.setMaxAge(0); // 立刻删除
        c.setPath("/");
        WebUtils.toHttp(response).addCookie(c); // 写入token
    }
    
    /**
     * 可通过在登出url后添加redirect参数实现登出后重定向
     */
    @Override
    protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
    	String redirectUrl = request.getParameter("redirect");
    	return StringUtils.isNotEmpty(redirectUrl) ? redirectUrl : super.getRedirectUrl(request, response, subject);
    }
}
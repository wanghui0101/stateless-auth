package com.github.sa.cas.web.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.sa.cas.authc.CaptchaUsernamePasswordToken;
import com.github.sa.cas.captcha.IncorrectCaptchaException;
import com.github.sa.cas.captcha.service.CaptchaService;
import com.github.sa.core.Account;
import com.github.sa.core.accesstoken.AccessTokenAware;
import com.github.sa.core.accesstoken.AccessTokenGenerator;
import com.github.sa.core.service.SessionService;
import com.google.common.collect.ImmutableMap;

/**
 * 登录认证过滤器
 * 
 * @author wh
 * @lastModified 2016-6-7 16:24:24
 */
public class StatelessLoginAuthcFilter extends FormAuthenticationFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(StatelessLoginAuthcFilter.class);
    
    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
    public static final String INDEX_URL = "/";
    
    private String captchaParam = DEFAULT_CAPTCHA_PARAM; // 对应页面中校验码input的name
    
    private String indexUrl = INDEX_URL; // 主页url
    
    @Autowired
    private CaptchaService captchaService;
    
    @Autowired
    private SessionService sessionService;
    
    @Autowired
    private AccessTokenGenerator accessTokenGenerator;
    
    @Autowired
    private AccessTokenAware accessTokenAware;
       
    public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}

	/**
     * 重写创建令牌的逻辑：在令牌中添加对验证码的支持
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        String captcha = getCaptcha(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        String captchaToken = WebUtils.getCleanParam(request, "captchaToken");
        return new CaptchaUsernamePasswordToken(username, password, rememberMe, host, captcha, captchaToken);
    }
    
    @Override
    protected boolean onAccessDenied(ServletRequest request,
            ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request, response);
            } else {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login page view.");
                }
                
                // 重写 FormAuthenticationFilter.onAccessDenied()
                // 默认是当以非POST方式访问loginUrl时(见FormAuthenticationFilter.isLoginSubmission()), 直接跳转到登录页
                // 这里添加判断: 若未登录, 则跳到登录页; 若已登录, 则跳到主页
                return redirectToIndex(request, response);
            }
        } else {
            if (logger.isTraceEnabled()) {
                logger.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }

            saveRequestAndRedirectToLogin(request, response);
            return false;
        }
    }

    protected boolean redirectToIndex(ServletRequest request, ServletResponse response) throws IOException {
        logger.debug(WebUtils.toHttp(request).getRequestURI());
        String accessToken = accessTokenAware.getAccessToken(request);
        if (sessionService.isLogged(accessToken)) { // 已登录
            WebUtils.issueRedirect(request, response, indexUrl);
            return false;
        }
        return true; // 过滤链继续, 重定向到登录页
    }

    /**
     * 重写执行登录的逻辑：添加对验证码校验的支持。如校验不通过，则抛出IncorrectCaptchaException
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        logger.debug(WebUtils.toHttp(request).getRequestURI());
        
        AuthenticationToken token = createToken(request, response);
        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                    "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }
        
        try {
            validateCaptcha((HttpServletRequest) request, (CaptchaUsernamePasswordToken) token); // 校验验证码
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }
    
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token,
            Subject subject, ServletRequest request, ServletResponse response)
            throws Exception {
    	Account currentUser = (Account) subject.getPrincipal();
    	
    	sessionService.kickoutLoggedIn(currentUser.getId()); // 踢出之前登录的同一用户
        
        // 将userId编码成访问令牌
        String accessToken = accessTokenGenerator.encode(currentUser.getId());
        sessionService.record(accessToken, currentUser); // 记录登录账户信息
        
        WebUtils.issueRedirect(request, response, getSuccessUrl(), ImmutableMap.of(accessTokenAware.getAccessTokenKey(), accessToken));
        return false; // prevent the chain from continuing
    }
    
    private void validateCaptcha(HttpServletRequest request, CaptchaUsernamePasswordToken token) {
        String captcha = token.getCaptcha();
        Boolean isResponseCorrect = captchaService.validate(token.getCaptchaToken(), captcha);
        if (!isResponseCorrect) {
            throw new IncorrectCaptchaException();
        }
    }
    
    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, captchaParam);
    }
    
}

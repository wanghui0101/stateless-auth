package com.github.sa.cas.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.sa.cas.captcha.IncorrectCaptchaException;
import com.github.sa.core.Account;
import com.github.sa.core.accesstoken.AccessTokenAware;
import com.github.sa.core.util.Response;

/**
 * 登录管理Controller 
 * 
 * @author Wh
 * @lastModified 2015-7-1 11:50:06
 */
@RequestMapping("/login")
@Controller
public class LoginController {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private AccessTokenAware accessTokenAware;
    
    /**
     * 跳转至登录页面
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
    	
    	/*
    	 * 如发现访问登录页面时还有访问令牌, 则需清除
    	 * 这种情况可能会发生在redis中的session失效, 此时刷新页面会跳到登录页, 但浏览器中的Cookie还未清除, 这样会导致后续登录中出现问题
    	 */
    	if (StringUtils.isNotEmpty(accessTokenAware.getAccessToken(request))) { 
    		Cookie accessTokenCookieToDelete = new Cookie(accessTokenAware.getAccessTokenKey(), null);
    		accessTokenCookieToDelete.setMaxAge(0); // 立刻删除
    		accessTokenCookieToDelete.setPath("/");
            response.addCookie(accessTokenCookieToDelete);
    	}
        model.addAttribute("captchaToken", RandomStringUtils.randomAlphanumeric(36)); // 验证码key
        return "login";
    }
    
    /**
     * 登录成功
     * 
     * @param currentAccount 当前登录用户. 其值是通过com.github.sa.core.support.method.AccountMethodArgumentResolver自动注入的
     * @param redirect 登录成功后重定向的url
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/success", method = RequestMethod.GET)
    @ResponseBody
    public Response success(Account currentAccount, @RequestParam(required = false) String redirect, HttpServletRequest request, HttpServletResponse response) {
        logger.info("{} login successfully.", currentAccount.getName());
        Cookie accessTokenCookie = new Cookie(accessTokenAware.getAccessTokenKey(), accessTokenAware.getAccessToken(request));
        accessTokenCookie.setMaxAge(-1); // 浏览器关闭时删除
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
        return Response.success(StringUtils.defaultString(redirect, request.getContextPath()), null);
    }
    
    /**
     * 登录失败
     * 
     * @param username
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Response failure(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String username, HttpServletRequest request) {
        String exceptionClassName = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        logger.error("{} login fail with exception {}", username, exceptionClassName);
        if (IncorrectCaptchaException.class.getName().equals(exceptionClassName)) {
            return Response.failure(null, "验证码错误或过期！");
        } else {
            return Response.failure(null, "用户名或密码错误！");
        }
    }
}

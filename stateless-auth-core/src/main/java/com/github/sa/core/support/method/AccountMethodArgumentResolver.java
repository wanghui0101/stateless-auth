package com.github.sa.core.support.method;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.github.sa.core.Account;
import com.github.sa.core.AccountUtils;

/**
 * 可使SpringMVC在Controller方法的参数列表中自动注入当前登录用户信息
 * 
 * @author wh
 * @lastModified 2016-6-6 09:35:43
 */
public class AccountMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
    	return parameter.getParameterType().isAssignableFrom(Account.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, 
    		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return AccountUtils.getCurrentAccount();
    }

}

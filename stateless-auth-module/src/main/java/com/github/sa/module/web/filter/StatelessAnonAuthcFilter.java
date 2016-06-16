package com.github.sa.module.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 如有访问令牌，则认证；如无访问令牌或认证失败，则继续执行（按匿名访问处理）
 * 
 * @author wh
 * @lastModified 2016-3-7 11:38:56
 */
public class StatelessAnonAuthcFilter extends StatelessAccessAuthcFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(StatelessAnonAuthcFilter.class);

	@Override
	protected Boolean onStatelessAuthcFailure(ServletRequest request,
			ServletResponse response, AuthenticationException e) {
		return !super.onStatelessAuthcFailure(request, response, e);
	}
	
	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		logger.debug("AccessToken DOES NOT exist, StatelessAnonAuthcFilter lets it go forward.");
		return true;
	}
	
}

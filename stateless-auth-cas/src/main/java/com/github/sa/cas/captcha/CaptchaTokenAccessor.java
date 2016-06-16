package com.github.sa.cas.captcha;

import javax.servlet.ServletRequest;

public class CaptchaTokenAccessor implements CaptchaTokenAware {
	
	private static final String CAPTCHA_TOKEN_KEY = "captchaToken";
	
	@Override
	public String getCaptchaToken(ServletRequest request) {
		return request.getParameter(CAPTCHA_TOKEN_KEY);
	}
	
}

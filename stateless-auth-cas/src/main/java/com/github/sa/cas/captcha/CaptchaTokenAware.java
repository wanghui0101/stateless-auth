package com.github.sa.cas.captcha;

import javax.servlet.ServletRequest;

/**
 * 用于获取验证码令牌的接口
 * 
 * @author wh
 * @lastModified 2016-6-7 16:11:50
 */
public interface CaptchaTokenAware {

	String getCaptchaToken(ServletRequest request);
	
}

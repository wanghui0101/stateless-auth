package com.github.sa.cas.captcha;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 扩展认证异常，定义错误的验证码异常
 * 
 * @author wh
 * @lastModified 2016-6-7 16:15:36
 */
@SuppressWarnings("serial")
public class IncorrectCaptchaException extends AuthenticationException {

    public IncorrectCaptchaException() {
        super();
    }

    public IncorrectCaptchaException(String message) {
        super(message);
    }

    public IncorrectCaptchaException(Throwable cause) {
        super(cause);
    }

    public IncorrectCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
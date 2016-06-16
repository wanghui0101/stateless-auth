package com.github.sa.cas.authc;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 对默认Token添加验证码支持
 * 
 * @author wh
 * @lastModified 2016-6-7 16:11:25
 */
@SuppressWarnings("serial")
public class CaptchaUsernamePasswordToken extends UsernamePasswordToken {
    
    private String captcha;
    
    private String captchaToken;
    
    public CaptchaUsernamePasswordToken(String username, String password,
            boolean rememberMe, String host, String captcha, String captchaToken) {
        super(username, password.toCharArray(), rememberMe, host);
        this.captcha = captcha;
        this.captchaToken = captchaToken;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCaptchaToken() {
        return captchaToken;
    }

    public void setCaptchaToken(String captchaToken) {
        this.captchaToken = captchaToken;
    }

}

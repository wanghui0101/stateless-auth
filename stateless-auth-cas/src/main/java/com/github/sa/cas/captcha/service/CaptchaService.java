package com.github.sa.cas.captcha.service;

import java.awt.image.BufferedImage;

/**
 * 验证码服务接口
 * 
 * @author wh
 * @lastModified 2016-6-7 16:16:32
 */
public interface CaptchaService {

	BufferedImage createImage(String token);

    boolean validate(String token, String captchaText);
    
}

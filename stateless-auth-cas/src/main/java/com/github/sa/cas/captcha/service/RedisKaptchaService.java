package com.github.sa.cas.captcha.service;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.sa.core.util.RedisOps;
import com.google.code.kaptcha.Producer;

/**
 * 基于Kaptcha框架实现的验证码，验证码的文本会存入redis
 * 
 * @author wh
 * @lastModified 2016-6-7 16:17:13
 */
public class RedisKaptchaService implements CaptchaService {

    private static final Logger logger = LoggerFactory.getLogger(RedisKaptchaService.class);
    
    private static final String KAPTCHA_KEY = "kaptcha";
    
    private String kaptchaKey = KAPTCHA_KEY;
    
    private long timeoutOfSeconds;
    
    @Autowired
    private Producer producer;
    
    @Autowired
    private RedisOps redisOps;
    
    public void setKaptchaKey(String kaptchaKey) {
		this.kaptchaKey = kaptchaKey;
	}

	public void setTimeoutOfSeconds(long timeoutOfSeconds) {
		this.timeoutOfSeconds = timeoutOfSeconds;
	}

	@Override
    public BufferedImage createImage(String captchaToken) {
        String captchaText = producer.createText();
        logger.debug("captchaToken {} -> catpcha {}", captchaToken, captchaText);
        redisOps.getValueOps().set(toSessionToken(captchaToken), captchaText, timeoutOfSeconds, TimeUnit.SECONDS);
        return producer.createImage(captchaText);
    }

    @Override
    public boolean validate(String token, String captchaText) {
        return StringUtils.equals(redisOps.getValueOps().get(toSessionToken(token)), captchaText);
    }
    
    private String toSessionToken(String token) {
        return kaptchaKey + ":" + token;
    }

}
package com.github.sa.cas.web.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.sa.cas.captcha.CaptchaTokenAware;
import com.github.sa.cas.captcha.service.CaptchaService;
import com.github.sa.cas.web.util.Servlets;

@Controller
public class CaptchaImageCreater {
    
    @Autowired
    private CaptchaService captchaService;
    
    @Autowired
    private CaptchaTokenAware captchaTokenAware;
    
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String captchaToken = captchaTokenAware.getCaptchaToken(request);
        Assert.notNull(captchaToken, "CaptchaToken DOES NOT exist.");
        
        BufferedImage image = captchaService.createImage(captchaToken);
        
		Servlets.setExpiresHeader(response, 0L);
		Servlets.setNoCacheHeader(response);
		response.setContentType("image/jpeg");
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		out.flush();
		out.close();
    }

}

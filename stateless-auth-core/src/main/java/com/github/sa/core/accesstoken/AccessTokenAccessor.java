package com.github.sa.core.accesstoken;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class AccessTokenAccessor implements AccessTokenAware {
	
	private String accessTokenKey;
	
    public void setAccessTokenKey(String accessTokenKey) {
		this.accessTokenKey = accessTokenKey;
	}

	@Override
    public String getAccessToken(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String accessToken = "";
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (getAccessTokenKey().equals(c.getName())) { // 尝试从Cookie中获取
                    accessToken = c.getValue();
                    break;
                }
            }
        }
        if (StringUtils.isEmpty(accessToken)) { // cookie中没取到从请求中取
            accessToken = StringUtils.defaultString(request.getParameter(getAccessTokenKey()));
        }
        return accessToken;
    }

	@Override
	public String getAccessTokenKey() {
		return accessTokenKey;
	}
    
}

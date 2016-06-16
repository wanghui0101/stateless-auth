package com.github.sa.core.accesstoken;

import javax.servlet.ServletRequest;

/**
 * <p>用于获取访问令牌的接口
 * 
 * <p>可以通过此接口实现定义自己的访问令牌key，及获取访问令牌的方法
 * 
 * @author wh
 * @lastModified 2016-6-7 15:44:26
 */
public interface AccessTokenAware {
	
	String getAccessTokenKey();

	String getAccessToken(ServletRequest request);

}

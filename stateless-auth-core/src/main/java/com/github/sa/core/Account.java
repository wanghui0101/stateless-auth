package com.github.sa.core;

import java.io.Serializable;

/**
 * 抽象的账户接口，业务系统需要实现此接口以表明一个账户对象
 * 
 * @author wh
 * @lastModified 2016-6-7 15:34:23
 */
public interface Account extends Serializable {
	
	String ACCESS_TOKEN = "accessToken"; // 访问令牌
	String ID = "id"; // 账户ID
	String ACCOUNT_NAME = "accountName"; // 登录名
	String NAME = "name"; // 中文名
	String ROLES = "roles"; // 角色
	String PERMISSIONS = "permissions"; // 权限
	
	String getId();
	
	String getAccountName();
	
	String getName();
	
	String getPassword();
	
	String getSalt();
	
	void setAccessToken(String accessToken);
	
	String getAccessToken();
	
}

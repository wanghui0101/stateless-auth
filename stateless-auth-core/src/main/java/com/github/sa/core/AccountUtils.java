package com.github.sa.core;

import org.apache.shiro.SecurityUtils;

/**
 * 基于Shiro的Subject概念的一些快捷工具方法
 * 
 * @author wh
 * @lastModified 2016-6-7 15:42:44
 */
public abstract class AccountUtils {

	public static Account getCurrentAccount() {
        return (Account) SecurityUtils.getSubject().getPrincipal();
    }
    
    public static Boolean logged() {
        return SecurityUtils.getSubject().isAuthenticated();
    }
    
    public static String getId() {
    	return getCurrentAccount().getId();
    }
    
}

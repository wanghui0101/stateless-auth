package com.github.sa.core.service;

import java.util.List;

import com.github.sa.core.Account;

/**
 * Session服务接口
 * 
 * @author wh
 * @lastModified 2016-6-7 15:56:59
 */
public interface SessionService {

	/**
	 * 记录用户信息
	 * @param accessToken
	 * @param account
	 */
	void record(String accessToken, Account account);
    
	/**
	 * 踢出在线用户
	 * @param userId
	 */
    void kickoutLoggedIn(String userId);
    
    /**
     * 根据访问令牌获取账户信息
     * @param accessToken
     * @return
     */
    Account retrieve(String accessToken);
    
    /**
     * session实现
     * @param accessToken
     */
    void invalidate(String accessToken);

    /**
     * 根据令牌判断用户是否在登录状态
     * @param accessToken
     * @return
     */
    Boolean isLogged(String accessToken);
    
    /**
     * 获取用户角色
     * @param accessToken
     * @return
     */
    List<String> getRoles(String accessToken);
    
    /**
     * 获取用户权限
     * @param accessToken
     * @return
     */
    List<String> getPermissions(String accessToken);
    
    /**
     * 判断是否用户某角色
     * @param accessToken
     * @param permisson
     * @return
     */
    Boolean[] hasRoles(String accessToken, String... role);
    
    /**
     * 判断是否用户某权限
     * @param accessToken
     * @param permisson
     * @return
     */
    Boolean[] hasPermissions(String accessToken, String... permisson);
    
}

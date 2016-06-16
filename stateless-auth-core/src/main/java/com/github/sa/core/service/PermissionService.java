package com.github.sa.core.service;

import java.util.List;

import com.github.sa.core.Account;

public interface PermissionService {

	/**
	 * 通过账户ID查询角色名称
	 * @param accountId
	 * @return
	 */
	List<String> getRoleNamesByAccountId(String accountId);
	
	/**
	 * 通过账户角色查询操作权限
	 * @param roleNames
	 * @return
	 */
	List<String> getPermissionsByRoleNames(List<String> roleNames);
	
	/**
	 * 通过账户ID查询操作权限
	 * @param roleNames
	 * @return
	 */
	List<String> getPermissionsByAccountId(String accountId);
	
	/**
	 * 通过权限查询角色名称
	 * @param permissions
	 * @return
	 */
	List<String> getRoleNamesByPermissions(List<String> permissions);
	
	/**
	 * 根据角色查询账户
	 * @param roleNames
	 * @return
	 */
	Account getAccountByRoleNames(List<String> roleNames);
	
	/**
	 * 根据权限查询账户
	 * @param permissions
	 * @return
	 */
	Account getAccountByPermissions(List<String> permissions);
}

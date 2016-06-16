package com.github.sa.core.service;

import java.util.List;

import com.github.sa.core.Account;

/**
 * 可由子类按需重写, 而不必重写接口的所有方法
 * 
 * @author wh
 * @lastModified 2016-3-7 16:54:52
 */
public abstract class PermissionAdapter implements PermissionService {

	@Override
	public List<String> getRoleNamesByAccountId(String accountId) {
		return null;
	}

	@Override
	public List<String> getPermissionsByRoleNames(List<String> roleNames) {
		return null;
	}

	@Override
	public List<String> getPermissionsByAccountId(String accountId) {
		return null;
	}

	@Override
	public List<String> getRoleNamesByPermissions(List<String> permissions) {
		return null;
	}

	@Override
	public Account getAccountByRoleNames(List<String> roleNames) {
		return null;
	}

	@Override
	public Account getAccountByPermissions(List<String> permissions) {
		return null;
	}
	
}

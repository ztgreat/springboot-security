package com.springboot.security.entity.ins;

import com.springboot.security.auth.UserToken;

import java.util.List;

/**
 * 用户登录后返回实体
 * 
 * @author Administrator
 *
 */
public class SysUserInfo {
	/****
	 * 当前用户角色信息
	 */
	private List<String> currentAuthority;

	/**
	 * 当前用户信息
	 */
	private UserToken currentUser;

	public List<String> getCurrentAuthority() {
		return currentAuthority;
	}

	public void setCurrentAuthority(List<String> currentAuthority) {
		this.currentAuthority = currentAuthority;
	}

	public UserToken getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserToken currentUser) {
		this.currentUser = currentUser;
	}

}

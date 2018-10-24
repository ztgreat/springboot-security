package com.springboot.security.entity.ins;

import com.springboot.security.auth.UserToken;

import java.util.Date;
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

	private Integer id;

	/**
	 * 登录帐号
	 */
	private String username;

	/**
	 * 用户昵称
	 */
	private String nickname;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 最后登录时间
	 */
	private Date lastLoginTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
}

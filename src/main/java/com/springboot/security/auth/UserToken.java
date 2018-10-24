package com.springboot.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * 登录用户信息
 */
public class UserToken extends User {


	private Integer id;

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



	public UserToken() {
		super(null,null,null);
	}


	public UserToken(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public UserToken(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public Integer getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setId(Integer id) {
		this.id = id;
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

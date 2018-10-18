package com.springboot.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 用户登录后返回实体
 * 
 *
 */
public class UserToken extends User {


	private Integer id;

	/**
	 * 用户昵称
	 */
	private String nickname;

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
}

package com.springboot.security.auth;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 获取登录用户信息
 * 注销登录
 * @author ztgreat
 */
public class TokenManager {

	private  static SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
	/**
	 * 获取当前登录的用户User对象
	 * @return
	 */
	public static UserToken getToken(){
		UserToken token = (UserToken)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return token ;
	}

	/**
	 * 获取当前用户NAME
	 * @return
	 */
	public static String getNickname(){
		return getToken().getNickname();
	}
	/**
	 * 获取当前用户ID
	 * @return
	 */
	public static Integer getUserId(){
		return getToken()==null?null:getToken().getId();
	}


	/**
	 * 退出登录
	 * @param request
	 * @param response
	 */
	public static void logout(HttpServletRequest request, HttpServletResponse response){
		logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
	}
}

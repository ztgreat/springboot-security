package com.springboot.security.controller;

import com.springboot.security.auth.TokenManager;
import com.springboot.security.base.ResponseEntity;
import com.springboot.security.entity.SysUser;
import com.springboot.security.auth.UserToken;
import com.springboot.security.service.SysRoleService;
import com.springboot.security.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录
 */
@Controller
public class LoginCol {

	@RequestMapping(value = "/api/logout", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
		ResponseEntity<String> res = new ResponseEntity<String>();
		try{

			TokenManager.logout(request,response);
		}catch (Exception ignore){
			return  res;
		}
		res.setSuccess("登出成功");
		return res;
	}

}

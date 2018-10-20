package com.springboot.security.controller;

import com.springboot.security.base.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 不拦截url 测试
 */
@Controller
public class TestCol {

	@RequestMapping(value = "/api/test", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
		ResponseEntity<String> res = new ResponseEntity<String>();
		res.setSuccess("拦截url 测试");
		return res;
	}

}

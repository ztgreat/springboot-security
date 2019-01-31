package com.springboot.security.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.springboot.security.auth.TokenManager;
import com.springboot.security.base.CommonConstant;
import com.springboot.security.base.ResponseEntity;
import com.springboot.security.base.ResponseList;
import com.springboot.security.base.ResponsePage;
import com.springboot.security.entity.SysUser;
import com.springboot.security.auth.UserToken;
import com.springboot.security.entity.ins.SysUserInfo;
import com.springboot.security.service.SysRoleService;
import com.springboot.security.service.SysUserService;
import com.springboot.security.util.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统用户控制层
 *
 * @author lpf
 */
@Controller
@RequestMapping(value = "/api/admin")
public class SysUserCol {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;

	@RequestMapping(value = "page", method = RequestMethod.GET)
	@ResponseBody
	public ResponsePage<SysUser> page(@RequestParam(value = "current", defaultValue = "1") int current,
									  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
									  @RequestParam(value = "search", defaultValue = "") String search,
									  @RequestParam(value = "status", defaultValue = "") String status) {
		ResponsePage<SysUser> res = new ResponsePage<SysUser>();
		try {
			IPage<SysUser> page = sysUserService.page(current, pageSize, search);
			res.setPage(page);
		} catch (RuntimeException e) {
			LoggerUtils.error(getClass(),"[sysUser page]" + e.getMessage());
			res.failure("获取失败");
		}
		return res;
	}


	@RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> save(@RequestBody SysUser user) {
		ResponseEntity<String> res = new ResponseEntity<String>();
		try {

			sysUserService.saveUser(user);

			// 关闭权限菜单时，为所有新建用户插入管理员角色
			// sysRoleService.updateUserRole(user.getId().toString(), "1");

			res.setMsg(CommonConstant.Message.OPTION_SUCCESS);
		} catch (RuntimeException e) {
			res.setMsg(CommonConstant.Message.OPTION_FAILURE);
			LoggerUtils.error(getClass(),"[sysUser save]" + e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> update(HttpServletRequest request, @RequestBody SysUser user) {
		ResponseEntity<String> res = new ResponseEntity<String>();
		try {
			user.setModifyTime(new Date());
			user.setPassword(null);
			sysUserService.updateById(user);
			res.setMsg(CommonConstant.Message.OPTION_SUCCESS);
		} catch (RuntimeException e) {
			res.setMsg(CommonConstant.Message.OPTION_FAILURE);
			LoggerUtils.error(getClass(),"[sysUser update]" + e.getMessage());
		}
		return res;
	}

	// 删除
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> delete(@RequestBody Map<String, Object> param) {
		ResponseEntity<String> res = new ResponseEntity<>();
		try {
			List<Integer> ids = (List<Integer>) param.get("ids");
			String msg = sysUserService.delete(ids);
			res.success(msg);
		} catch (Exception e) {
			LoggerUtils.error(getClass(),"[ sysuser delete]" + e.getMessage());
			res.failure(CommonConstant.Message.OPTION_FAILURE);
		}
		return res;

	}

	/**
	 * 获取登录用户信息
	 * @return
	 */
	@RequestMapping(value = "/query")
	@ResponseBody
	public ResponseEntity<SysUserInfo> query() {
		ResponseEntity<SysUserInfo> res = new ResponseEntity<SysUserInfo>();
		try {
			UserToken token =TokenManager.getToken();
			SysUserInfo su = new SysUserInfo(token);
			res.setData(su);
		} catch (Exception e) {
			LoggerUtils.error(getClass(),"获取用户登录信息失败");
			res.failure(CommonConstant.Message.OPTION_FAILURE);
		}
		return res;
	}

}

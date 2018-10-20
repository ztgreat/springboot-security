package com.springboot.security.auth.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.security.auth.TokenManager;
import com.springboot.security.auth.UserToken;
import com.springboot.security.base.ResponseEntity;
import com.springboot.security.entity.ins.SysUserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录成功handler
 * @author ztgreat
 */
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        /**
         * 登录成功,将部分用户登录信息返回给前端
         */

        response.setContentType("application/json;charset=utf-8");
        SysUserInfo su = new SysUserInfo();
        UserToken token = TokenManager.getToken();
        su.setUsername(token.getUsername());
        su.setNickname(token.getNickname());
        su.setId(token.getId());
        ResponseEntity<SysUserInfo>res= new ResponseEntity<>();
        res.setSuccess("登录成功");
        res.setData(su);
        ObjectMapper om = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.write(om.writeValueAsString(res));
        out.flush();
        out.close();
    }
}

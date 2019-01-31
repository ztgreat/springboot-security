package com.springboot.security.auth.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.security.base.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录失败handler
 * @author ztgreat
 */
public class CustomLgoinFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler{
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        response.setContentType("application/json;charset=utf-8");
        ResponseEntity<String> res = new ResponseEntity<>();
        if (e instanceof BadCredentialsException ||
                e instanceof UsernameNotFoundException) {
            res.failure("账户名或者密码输入错误!");
        } else if (e instanceof LockedException) {
            res.failure("账户被锁定，请联系管理员!");
        } else if (e instanceof CredentialsExpiredException) {
            res.failure("密码过期，请联系管理员!");
        } else if (e instanceof AccountExpiredException) {
            res.failure("账户过期，请联系管理员!");
        } else if (e instanceof DisabledException) {
            res.failure("账户被禁用，请联系管理员!");
        } else {
            res.failure("登录失败!");
        }
        response.setStatus(401);
        ObjectMapper om = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.write(om.writeValueAsString(res));
        out.flush();
        out.close();
    }
}

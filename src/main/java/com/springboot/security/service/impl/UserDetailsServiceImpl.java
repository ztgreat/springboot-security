package com.springboot.security.service.impl;

import com.springboot.security.entity.SysRole;
import com.springboot.security.entity.SysUser;
import com.springboot.security.auth.UserToken;
import com.springboot.security.service.SysRoleService;
import com.springboot.security.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private  SysUserService userService;

    @Autowired
    private SysRoleService sysRoleService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.getUserByUserName(username);
        if (user == null){
            throw new UsernameNotFoundException("用户不存在！");
        }

        //根据用户ID查询角色（role），放入到Authorization里。
        List<SysRole> roles = sysRoleService.getRoleByUserId(user.getId());
        Set<String> sr=new HashSet<String>();
        for (SysRole sysRole : roles) {
            sr.add(sysRole.getCode());
        }
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = createAuthorities(sr);
        UserToken userToken = new UserToken(user.getUsername(), user.getPassword(), simpleGrantedAuthorities);
        userToken.setId(user.getId());
        userToken.setUsername(user.getUsername());
        userToken.setNickname(user.getNickname());
        userToken.setEmail(user.getEmail());
        userToken.setCreateTime(user.getCreateTime());
        userToken.setLastLoginTime(user.getLastLoginTime());
        //单独存一份角色信息
        userToken.setRoles(roles);
        return userToken;
    }

    /**
     * 权限字符串转化
     *
     * 如 "USER,ADMIN" -> SimpleGrantedAuthority("USER") + SimpleGrantedAuthority("ADMIN")
     *
     * @param roleStr 权限字符串
     */
    private List<SimpleGrantedAuthority> createAuthorities(String roleStr){
        String[] roles = roleStr.split(",");
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for (String role : roles) {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        return simpleGrantedAuthorities;
    }

    private List<SimpleGrantedAuthority> createAuthorities(Set<String> roles){
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for (String role : roles) {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        return simpleGrantedAuthorities;
    }

}

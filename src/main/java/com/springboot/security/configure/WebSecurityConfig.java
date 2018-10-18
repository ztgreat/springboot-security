package com.springboot.security.configure;

import com.springboot.security.auth.CustomAuthenticationFailureHandler;
import com.springboot.security.auth.CustomAuthenticationFilter;
import com.springboot.security.auth.CustomAuthenticationSuccessHandler;
import com.springboot.security.auth.CustomAuthenticationProvider;
import com.springboot.security.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configurable
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{


    //自定义用户服务
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    //自定义验证
//    @Autowired
//    private CustomAuthenticationProvider authenticationProvider;

    /**
     * 匹配 "/" 路径，不需要权限即可访问
     * 匹配 "/user" 及其以下所有路径，都需要 "USER" 权限
     * 登录地址为 "/login"，登录成功默认跳转到页面 "/user"
     * 退出登录的地址为 "/logout"，退出成功后跳转到页面 "/login"
     * 默认启用 CSRF
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated();
//                .and()
//                .formLogin().loginPage("/api/login").permitAll()
//                .failureHandler(new CustomAuthenticationFailureHandler())
//                .successHandler(new CustomAuthenticationSuccessHandler());
//                .and()
//                .logout().logoutUrl("/api/logout").permitAll();

        http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    //注册自定义的UsernamePasswordAuthenticationFilter
    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();

        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));

        filter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
        filter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());

        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }


    /**
     * 添加 UserDetailsService， 实现自定义登录校验
     */
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception{
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

//        builder.authenticationProvider(authenticationProvider);

    }

    /**
     * 密码加密
     */
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

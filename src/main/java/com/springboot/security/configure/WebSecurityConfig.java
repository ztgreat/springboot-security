package com.springboot.security.configure;

import com.springboot.security.auth.CustomAuthenticationEntryPoint;
import com.springboot.security.auth.login.CustomLgoinFailureHandler;
import com.springboot.security.auth.login.CustomLoginAuthenticationFilter;
import com.springboot.security.auth.login.CustomLoginSuccessHandler;
import com.springboot.security.auth.url.UrlAccessDecisionManager;
import com.springboot.security.auth.url.UrlMetadataSource;
import com.springboot.security.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configurable
@EnableWebSecurity
@ConfigurationProperties(prefix = "spring.security")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{


    //自定义用户服务
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    //自定义验证
//    @Autowired
//    private CustomAuthenticationProvider authenticationProvider;


    /**
     * 放行接口
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //拦截忽略的地方,认证信息不会注入
        web.ignoring().antMatchers( "/api/test/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

    }

    //注册自定义的UsernamePasswordAuthenticationFilter
    @Bean
    CustomLoginAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomLoginAuthenticationFilter filter = new CustomLoginAuthenticationFilter();

        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));

        filter.setAuthenticationFailureHandler(new CustomLgoinFailureHandler());
        filter.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler());

        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
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

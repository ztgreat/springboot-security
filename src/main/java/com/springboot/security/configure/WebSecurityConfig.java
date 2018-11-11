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


    /**
     * 请求前缀,类似：/api/test/hello
     */
    private  String urlPrefix;

    /**
     * 退出登录url
     */
    private  String urlLogout;

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getUrlLogout() {
        return urlLogout;
    }

    public void setUrlLogout(String urlLogout) {
        this.urlLogout = urlLogout;
    }

    //自定义用户服务
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Autowired
    UrlMetadataSource urlMetadataSource;
    @Autowired
    UrlAccessDecisionManager urlAccessDecisionManager;


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



    /**
     * 匹配 "/" 路径，不需要权限即可访问
     * 匹配 "/user" 及其以下所有路径，都需要 "USER" 权限
     * 登录地址为 "/login"，登录成功默认跳转到页面 "/user"
     * 退出登录的地址为 "/logout"，退出成功后跳转到页面 "/login"
     * 默认启用 CSRF
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        this.urlMetadataSource.setUrlPrefix(this.getUrlPrefix());
        this.urlMetadataSource.setUrlLogout(this.getUrlLogout());

        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(urlMetadataSource);
                        o.setAccessDecisionManager(urlAccessDecisionManager);
                        return o;
                    }
                })
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());


        http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
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

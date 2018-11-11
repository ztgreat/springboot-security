package com.springboot.security.oauth2;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源服务器
 */
@Configuration
public class OAuth2ResourceServerConfig {

    private static final String DEMO_RESOURCE_ID = "order";

    @Configuration
    @EnableResourceServer
    @Order(110)
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
//            http
//                .authorizeRequests()
//                    .antMatchers("/order/**").authenticated();//配置order访问控制，必须认证过后才可以访问

        }
    }

}

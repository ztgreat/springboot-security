package com.springboot.security.session;


import com.springboot.security.configure.WebSecurityConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

    public SecurityInitializer() {
        super(WebSecurityConfig .class, HttpSessionConfig .class);
    }
}

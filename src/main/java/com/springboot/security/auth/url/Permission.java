package com.springboot.security.auth.url;


/**
 * 参考shiro 中的Permission
 */
public interface Permission {

    boolean implies(Permission p);
}

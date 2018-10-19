package com.springboot.security.auth.url;

public interface Permission {

    boolean implies(Permission p);
}

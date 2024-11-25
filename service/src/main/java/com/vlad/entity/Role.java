package com.vlad.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    CUSTOMER,
    CARRIER,
    ADMIN,
    GUEST;

    @Override
    public String getAuthority() {
        return name();
    }
}

package ru.demo.user.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN;

    public String getAuthority() {
        return name();
    }
}

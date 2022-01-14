package ru.javaops.voting.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        // https://stackoverflow.com/a/19542316
        return "ROLE_" + name();
    }
}

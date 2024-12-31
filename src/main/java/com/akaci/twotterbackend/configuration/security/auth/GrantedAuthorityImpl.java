package com.akaci.twotterbackend.configuration.security.auth;

import com.akaci.twotterbackend.persistence.entity.enums.Role;
import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl implements GrantedAuthority {

    private final Role role;

    public GrantedAuthorityImpl(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + role.name();
    }
}

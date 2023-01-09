package com.staff.application.enumeration;

import static com.staff.application.constant.Authority.ADMIN_AUTHORITIES;
import static com.staff.application.constant.Authority.USER_AUTHORITIES;

public enum Role {
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_USER(USER_AUTHORITIES);

    private final String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}

package com.ecsimsw.common.dto;

import java.util.Arrays;

public record AuthUser(
    String username,
    String[] roles
) {

    public boolean isAdmin() {
        return Arrays.stream(roles)
            .anyMatch("ADMIN"::equalsIgnoreCase);
    }
}

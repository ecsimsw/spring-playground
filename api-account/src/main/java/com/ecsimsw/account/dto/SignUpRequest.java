package com.ecsimsw.account.dto;

import com.ecsimsw.account.domain.User;

public record SignUpRequest(
    String username,
    String password,
    String email
) {

    public User toEntity() {
        return User.builder()
            .username(username)
            .email(email)
            .isAdmin(false)
            .build();
    }
}

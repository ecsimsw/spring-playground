package com.ecsimsw.account.dto;

import com.ecsimsw.domain.User;

public record SignUpRequest(
    String username,
    String email
) {
    public User toEntity() {
        return User.builder()
            .username(username)
            .email(email)
            .build();
    }
}

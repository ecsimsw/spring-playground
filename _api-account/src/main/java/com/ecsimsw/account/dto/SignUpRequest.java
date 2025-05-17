package com.ecsimsw.account.dto;

import com.ecsimsw.account.domain.User;
import com.ecsimsw.common.service.client.dto.AuthCreationRequest;

public record SignUpRequest(
    String username,
    String password,
    String email
) {

    public User toUser() {
        return new User(username, email, false);
    }

    public AuthCreationRequest toAuthCreationRequest(Long userId) {
        return new AuthCreationRequest(userId, username, password);
    }
}

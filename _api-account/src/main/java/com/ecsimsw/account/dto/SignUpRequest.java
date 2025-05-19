package com.ecsimsw.account.dto;

import com.ecsimsw.account.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public record SignUpRequest(
    String username,
    String password,
    String email
) {

    public User toUser() {
        return new User(username, email, false);
    }
}

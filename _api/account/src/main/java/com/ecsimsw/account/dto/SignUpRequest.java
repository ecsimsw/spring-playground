package com.ecsimsw.account.dto;

import com.ecsimsw.account.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public record SignUpRequest(
    String username,
    String password
) {

    public User toUser() {
        return new User(username);
    }
}

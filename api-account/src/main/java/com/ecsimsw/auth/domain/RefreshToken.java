package com.ecsimsw.auth.domain;

import com.ecsimsw.account.domain.User;
import com.ecsimsw.auth.utils.JwtUtils;

import java.util.Map;

import static com.ecsimsw.auth.config.SecurityConfig.REFRESH_TOKEN_EXPIRED_TIME;

public record RefreshToken(
    String username
) {
    public static RefreshToken of(User user) {
        return new RefreshToken(user.getUsername());
    }

    public String asJwtToken(String secretKey) {
        return JwtUtils.generate(secretKey, REFRESH_TOKEN_EXPIRED_TIME, Map.of("username", username));
    }
}

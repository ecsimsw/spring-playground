package com.ecsimsw.auth.domain;

import com.ecsimsw.auth.config.TokenConfig;
import com.ecsimsw.common.error.AuthException;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.support.JwtUtils;
import com.ecsimsw.domain.User;

import java.util.Map;

public record RefreshToken(
    String username
) {
    public static RefreshToken of(User user) {
        return new RefreshToken(user.getUsername());
    }

    public static RefreshToken fromToken(String secretKey, String token) {
        try {
            return new RefreshToken(JwtUtils.getClaimValue(secretKey, token, "username"));
        } catch (Exception e) {
            throw new AuthException(ErrorType.INVALID_TOKEN);
        }
    }

    public String asJwtToken(String secretKey) {
        return JwtUtils.generate(secretKey, TokenConfig.REFRESH_TOKEN_EXPIRED_TIME, Map.of("username", username));
    }
}

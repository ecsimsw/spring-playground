package com.ecsimsw.auth.domain;

import com.ecsimsw.common.error.AuthException;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.domain.User;
import com.ecsimsw.domain.support.JwtUtils;

import java.util.Map;

import static com.ecsimsw.common.config.TokenConfig.ACCESS_TOKEN_EXPIRED_TIME;

public record AccessToken(
    String username,
    boolean isAdmin
) {
    public static AccessToken of(User user) {
        return new AccessToken(user.getUsername(), user.isAdmin());
    }

    public static AccessToken fromToken(String secretKey, String token) {
        try {
            return new AccessToken(
                JwtUtils.getClaimValue(secretKey, token, "username"),
                Boolean.parseBoolean(JwtUtils.getClaimValue(secretKey, token, "isAdmin"))
            );
        } catch (Exception e) {
            throw new AuthException(ErrorType.INVALID_TOKEN);
        }
    }

    public String asJwtToken(String secretKey) {
        return JwtUtils.generate(secretKey, ACCESS_TOKEN_EXPIRED_TIME, Map.of(
            "username", username,
            "isAdmin", isAdmin
        ));
    }
}

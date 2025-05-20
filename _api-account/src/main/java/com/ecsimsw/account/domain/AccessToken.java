package com.ecsimsw.account.domain;

import com.ecsimsw.common.error.AuthException;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.support.utils.JwtUtils;

import java.util.Map;

import static com.ecsimsw.common.config.TokenConfig.ACCESS_TOKEN_EXPIRED_TIME;

public record AccessToken(
    String username,
    String ext
) {
    public static AccessToken fromToken(String secretKey, String token) {
        try {
            return new AccessToken(
                JwtUtils.getClaimValue(secretKey, token, "username"),
                JwtUtils.getClaimValue(secretKey, token, "ext")
            );
        } catch (Exception e) {
            throw new AuthException(ErrorType.INVALID_TOKEN);
        }
    }

    public String asJwtToken(String secretKey) {
        return JwtUtils.generate(
            secretKey,
            ACCESS_TOKEN_EXPIRED_TIME,
            Map.of(
                "username", username,
                "ext", ext
            )
        );
    }
}

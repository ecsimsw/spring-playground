package com.ecsimsw.common.domain;

import com.ecsimsw.common.error.ApiException;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.support.utils.JwtUtils;

import java.util.Map;

import static com.ecsimsw.common.config.TokenConfig.ACCESS_TOKEN_EXPIRED_TIME;

public record AccessToken(
    String username,
    String uid
) {
    public static AccessToken fromToken(String secretKey, String token) {
        try {
            return new AccessToken(
                JwtUtils.getClaimValue(secretKey, token, "username"),
                JwtUtils.getClaimValue(secretKey, token, "uid")
            );
        } catch (Exception e) {
            throw new ApiException(ErrorType.INVALID_TOKEN);
        }
    }

    public String asJwtToken(String secretKey) {
        return JwtUtils.generate(
            secretKey,
            ACCESS_TOKEN_EXPIRED_TIME,
            Map.of(
                "username", username,
                "uid", uid
            )
        );
    }
}

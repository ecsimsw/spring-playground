package com.ecsimsw.common.domain;

import com.ecsimsw.common.error.AuthException;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.support.JwtUtils;

public record AccessToken(
    String username,
    boolean isAdmin,
    String[] roles
) {
    public static AccessToken fromToken(String secretKey, String token) {
        try {
            return new AccessToken(
                JwtUtils.getClaimValue(secretKey, token, "username"),
                Boolean.parseBoolean(JwtUtils.getClaimValue(secretKey, token, "isAdmin")),
                JwtUtils.getClaimValue(secretKey, token, "roles").split(",")
            );
        } catch (Exception e) {
            throw new AuthException(ErrorType.INVALID_TOKEN);
        }
    }

}

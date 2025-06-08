package com.ecsimsw.account.dto;

public record AuthTokenResponse(
    String accessToken,
    String refreshToken
) {
}

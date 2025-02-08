package com.ecsimsw.auth.dto;

public record Tokens(
    String accessToken,
    String refreshToken
) {
}

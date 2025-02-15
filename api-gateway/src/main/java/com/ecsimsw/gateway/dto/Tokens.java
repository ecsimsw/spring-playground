package com.ecsimsw.gateway.dto;

public record Tokens(
    String accessToken,
    String refreshToken
) {
}

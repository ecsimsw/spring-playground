package com.ecsimsw.account.dto;

public record Tokens(
    String accessToken,
    String refreshToken
) {
}

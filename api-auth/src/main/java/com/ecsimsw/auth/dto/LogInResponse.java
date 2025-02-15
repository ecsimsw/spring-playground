package com.ecsimsw.auth.dto;

public record LogInResponse(
    Tokens tokens,
    boolean isTempPassword
) {
}

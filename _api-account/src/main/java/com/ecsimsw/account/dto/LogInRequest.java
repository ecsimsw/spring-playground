package com.ecsimsw.account.dto;

public record LogInRequest(
    String username,
    String password
) {
}

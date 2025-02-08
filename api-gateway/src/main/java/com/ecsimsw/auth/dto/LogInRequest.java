package com.ecsimsw.auth.dto;

public record LogInRequest(
    String username,
    String password
) {
}

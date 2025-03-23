package com.ecsimsw.common.dto;

public record SignUpRequest(
    String username,
    String password,
    String email
) {
}

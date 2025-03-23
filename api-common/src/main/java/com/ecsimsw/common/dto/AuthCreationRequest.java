package com.ecsimsw.common.dto;

public record AuthCreationRequest(
    Long userId,
    String username,
    String purePassword
) {
}

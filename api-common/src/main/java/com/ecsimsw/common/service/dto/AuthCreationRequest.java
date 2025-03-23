package com.ecsimsw.common.service.dto;

public record AuthCreationRequest(
    Long userId,
    String username,
    String purePassword
) {
}

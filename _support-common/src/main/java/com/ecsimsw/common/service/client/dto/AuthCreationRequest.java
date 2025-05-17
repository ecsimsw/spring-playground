package com.ecsimsw.common.service.client.dto;

public record AuthCreationRequest(
    Long userId,
    String username,
    String purePassword
) {
}

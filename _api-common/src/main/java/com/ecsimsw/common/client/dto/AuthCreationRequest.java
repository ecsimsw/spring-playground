package com.ecsimsw.common.client.dto;

public record AuthCreationRequest(
    Long userId,
    String username,
    String purePassword
) {
}

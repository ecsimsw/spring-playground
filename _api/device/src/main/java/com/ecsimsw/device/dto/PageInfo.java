package com.ecsimsw.device.dto;

public record PageInfo(
    Object startCursor,
    Object endCursor,
    boolean hasNext,
    boolean hasPrev
) {
}

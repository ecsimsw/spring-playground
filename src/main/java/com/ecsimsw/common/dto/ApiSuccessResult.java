package com.ecsimsw.common.dto;

public record ApiSuccessResult<T>(
    T result
) implements ApiResult {

    public static <T> ApiResult of(T result) {
        return new ApiSuccessResult<>(result);
    }
}

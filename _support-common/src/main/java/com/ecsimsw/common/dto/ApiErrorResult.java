package com.ecsimsw.common.dto;

import com.ecsimsw.common.error.ApiException;

public record ApiErrorResult(
    String code,
    String message
) implements ApiResult {

    public static ApiErrorResult of(ApiException e) {
        return new ApiErrorResult(e.code(), e.message());
    }
}

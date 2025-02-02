package com.ecsimsw.common.dto;

import com.ecsimsw.common.error.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class ApiResponse<T> extends ResponseEntity<T> {

    public ApiResponse(T body, HttpStatus status) {
        super(body, status);
    }

    public ApiResponse(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public ApiResponse(T body, MultiValueMap<String, String> headers, int rawStatus) {
        super(body, headers, rawStatus);
    }

    public ApiResponse(T body, MultiValueMap<String, String> headers, HttpStatus statusCode) {
        super(body, headers, statusCode);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(null, HttpStatus.OK);
    }

    public static ApiResponse<Void> success(HttpStatus status) {
        return new ApiResponse<>(null, status);
    }

    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(result, HttpStatus.OK);
    }

    public static <T> ApiResponse<T> success(HttpStatus status, T result) {
        return new ApiResponse<>(result, status);
    }

    public static ApiResponse<ApiErrorResult> error(ApiException e) {
        return new ApiResponse<>(ApiErrorResult.of(e), e.status());
    }
}

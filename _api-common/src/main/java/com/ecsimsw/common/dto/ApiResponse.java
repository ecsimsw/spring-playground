package com.ecsimsw.common.dto;

import com.ecsimsw.common.error.ApiException;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import static com.ecsimsw.common.config.LogConfig.MDC_TRACE_ID;
import static com.ecsimsw.common.config.LogConfig.TRACE_ID_HEADER;

public class ApiResponse<T> extends ResponseEntity<T> {

    public ApiResponse(T body, HttpStatus statusCode) {
        super(body, statusCode);
    }

    public ApiResponse(T body, MultiValueMap<String, String> headers, HttpStatus statusCode) {
        super(body, headers, statusCode);
    }

    public static ApiResponse<Void> success() {
        return success(HttpStatus.OK, null);
    }

    public static <T> ApiResponse<T> success(T result) {
        return success(HttpStatus.OK, result);
    }

    public static <T> ApiResponse<T> success(HttpStatus status, T result) {
        var headers = new HttpHeaders();
        headers.add(TRACE_ID_HEADER, MDC.get(MDC_TRACE_ID));
        return new ApiResponse<>(result, headers, status);
    }

    public static ApiResponse<ApiErrorResult> error(ApiException e) {
        var headers = new HttpHeaders();
        headers.add(TRACE_ID_HEADER, MDC.get(MDC_TRACE_ID));
        return new ApiResponse<>(ApiErrorResult.of(e), headers, e.status());
    }
}

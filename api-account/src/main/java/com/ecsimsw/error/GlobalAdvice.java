package com.ecsimsw.error;

import com.ecsimsw.common.dto.ApiErrorResult;
import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.error.ApiException;
import com.ecsimsw.common.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@RestControllerAdvice
public class GlobalAdvice {

    @ExceptionHandler(ApiException.class)
    public ApiResponse<ApiErrorResult> coreException(ApiException e) {
        return ApiResponse.error(e);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<ApiErrorResult> accessDenied() {
        var e = new ApiException(ErrorType.FORBIDDEN);
        return ApiResponse.error(e);
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestPartException.class,
        HttpRequestMethodNotSupportedException.class,
        HttpMessageNotReadableException.class
    })
    public ApiResponse<ApiErrorResult> invalidRequest() {
        var exception = new ApiException(ErrorType.INVALID_PARAMETER);
        return ApiResponse.error(exception);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<ApiErrorResult> unhandled(Exception e) {
        e.fillInStackTrace();
        log.error("unhandled : {}", e.getMessage());
        var exception = new ApiException(ErrorType.UNHANDLED);
        return ApiResponse.error(exception);
    }
}

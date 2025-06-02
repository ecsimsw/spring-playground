package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiErrorResult;
import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.error.ApiException;
import com.ecsimsw.common.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalAdvice {

    @ExceptionHandler(ApiException.class)
    public ApiResponse<ApiErrorResult> coreException(ApiException e) {
        return ApiResponse.error(e);
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestPartException.class,
        HttpRequestMethodNotSupportedException.class,
        HttpMessageNotReadableException.class,
        NoResourceFoundException.class
    })
    public ApiResponse<ApiErrorResult> invalidRequest(Exception e) {
        e.fillInStackTrace();
        var exception = new ApiException(ErrorType.INVALID_REQUEST);
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

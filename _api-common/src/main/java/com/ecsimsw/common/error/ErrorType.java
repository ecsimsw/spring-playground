package com.ecsimsw.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.E40001),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, ErrorCode.E40002),
    USER_NOT_APPROVED_YET(HttpStatus.BAD_REQUEST, ErrorCode.E40003),
    FAILED_TO_AUTHENTICATE(HttpStatus.UNAUTHORIZED, ErrorCode.E40004),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, ErrorCode.E40005),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, ErrorCode.E40006),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, ErrorCode.E40007),
    FORBIDDEN(HttpStatus.UNAUTHORIZED, ErrorCode.E40008),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, ErrorCode.E40009),
    UNHANDLED(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E50001),
    ;

    private final HttpStatus status;
    private final ErrorCode code;
    private final String message;

    ErrorType(HttpStatus status, ErrorCode code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    ErrorType(HttpStatus status, ErrorCode code) {
        var msg = name();
        msg = msg.replace("_", " ");
        msg = msg.toLowerCase();
        msg = Character.toUpperCase(msg.charAt(0)) + msg.substring(1);
        this.status = status;
        this.code = code;
        this.message = msg;
    }
}

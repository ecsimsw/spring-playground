package com.ecsimsw.device.error;

import com.ecsimsw.common.error.ApiException;
import com.ecsimsw.common.error.ErrorType;

public class NotificationException extends ApiException {

    public NotificationException(ErrorType type) {
        super(type);
    }
}

package com.ecsimsw.device.error;

import com.ecsimsw.common.error.ApiException;
import com.ecsimsw.common.error.ErrorType;

public class DeviceException extends ApiException {

    public DeviceException(ErrorType type) {
        super(type);
    }

    public DeviceException(ErrorType type, String message) {
        super(type, message);
    }
}

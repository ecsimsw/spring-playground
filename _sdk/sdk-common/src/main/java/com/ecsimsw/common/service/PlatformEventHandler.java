package com.ecsimsw.common.service;

import com.ecsimsw.common.dto.DeviceEventMessage;

public interface PlatformEventHandler {

//    void handle(DeviceEventMessage deviceEventMessage);

    void handle(String payload);
}

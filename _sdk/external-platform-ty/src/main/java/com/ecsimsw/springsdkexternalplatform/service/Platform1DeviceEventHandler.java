package com.ecsimsw.springsdkexternalplatform.service;

import com.ecsimsw.springsdkexternalplatform.dto.DeviceEventMessage;

public interface Platform1DeviceEventHandler {

    void handle(DeviceEventMessage deviceEventMessage);
}

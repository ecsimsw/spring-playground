package com.ecsimsw.sdkcommon.service;

import com.ecsimsw.sdkcommon.dto.event.DeviceEventMessage;
import com.ecsimsw.sdkcommon.dto.event.PairingEventMessage;

public interface PlatformEventHandler {

    void handleDeviceEvent(DeviceEventMessage deviceEventMessage);

    void handlePairingEvent(PairingEventMessage pairingEventMessage);
}

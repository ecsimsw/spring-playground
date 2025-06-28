package com.ecsimsw.sdkcommon.service;

import com.ecsimsw.sdkcommon.dto.DeviceEventMessage;
import com.ecsimsw.sdkcommon.dto.PairingEventMessage;

public interface PlatformEventHandler {

    void handle(DeviceEventMessage deviceEventMessage);

    void handlePairingEvent(PairingEventMessage pairingEventMessage);
}

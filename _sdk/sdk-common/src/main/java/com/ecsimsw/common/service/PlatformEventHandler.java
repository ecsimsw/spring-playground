package com.ecsimsw.common.service;

import com.ecsimsw.common.dto.PairingEventMessage;

public interface PlatformEventHandler {

//    void handle(DeviceEventMessage deviceEventMessage);

    void handle(String payload);

    void handlePairingEvent(PairingEventMessage pairingEventMessage);
}

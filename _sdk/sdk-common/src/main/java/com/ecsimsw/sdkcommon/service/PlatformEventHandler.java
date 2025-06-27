package com.ecsimsw.sdkcommon.service;

import com.ecsimsw.sdkcommon.dto.PairingEventMessage;

public interface PlatformEventHandler {

    void handle(String payload);

    void handlePairingEvent(PairingEventMessage pairingEventMessage);
}

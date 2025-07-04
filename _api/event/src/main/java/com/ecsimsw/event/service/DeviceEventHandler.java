package com.ecsimsw.event.service;

import com.ecsimsw.common.dto.DeviceAlertEvent;
import com.ecsimsw.common.dto.DeviceHistoryEvent;
import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.common.service.DeviceClient;
import com.ecsimsw.common.service.EventClient;
import com.ecsimsw.event.domain.DeviceOwnerRepository;
import com.ecsimsw.event.support.DeviceEventBrokerClient;
import com.ecsimsw.sdkcommon.domain.PlatformProducts;
import com.ecsimsw.sdkcommon.dto.event.DeviceEventMessage;
import com.ecsimsw.sdkcommon.dto.event.PairingEventMessage;
import com.ecsimsw.sdkcommon.service.PlatformEventHandler;
import com.ecsimsw.sdkty.domain.TyUserIdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceEventHandler implements PlatformEventHandler {

    private final DeviceOwnerRepository deviceOwnerRepository;
    private final DeviceEventBrokerClient deviceEventBrokerClient;
    private final TyUserIdRepository tyUserInfoRepository;
    private final DeviceClient deviceClient;
    private final EventClient eventClient;

    @Override
    public void handlePairingEvent(PairingEventMessage pairingEventMessage) {
        var userId = pairingEventMessage.userId();
        var tyUserInfo = tyUserInfoRepository.findById(userId).orElseThrow();
        deviceClient.refresh(tyUserInfo.getUsername());
        eventClient.refresh(tyUserInfo.getUsername());
    }

    @Override
    public void handleDeviceEvent(DeviceEventMessage eventMessage) {
        var productId = eventMessage.productId();
        if (!PlatformProducts.isSupported(productId)) {
            return;
        }
        var optDeviceOwner = deviceOwnerRepository.findById(eventMessage.deviceId());
        if (optDeviceOwner.isEmpty()) {
            return;
        }

        var deviceOwner = optDeviceOwner.get();
        log.info("Handle bind device : {} {}", deviceOwner.getDeviceId(), eventMessage.statuses());

        eventMessage.statuses().forEach(event -> {
            var product = PlatformProducts.getById(productId);
            if (product.hasStatusCode(event.code())) {
                log.info("Handle device status event : {} {}", deviceOwner.getDeviceId(), event.code());
                var statusEvent = new DeviceStatusEvent(eventMessage.deviceId(), event.code(), event.value(), eventMessage.timeStamp());
                deviceEventBrokerClient.produceDeviceStatus(statusEvent);
            }
            if (product.hasHistoryCode(event.code())) {
                log.info("Handle device history event : {} {}", deviceOwner.getDeviceId(), event.code());
                var historyEvent = new DeviceHistoryEvent(eventMessage.deviceId(), event.code(), event.value(), eventMessage.timeStamp());
                deviceEventBrokerClient.produceDeviceHistory(historyEvent);
            }
            if (product.hasAlertCode(event.code())) {
                log.info("Handle device alert event : {} {}", deviceOwner.getDeviceId(), event.code());
                var alertEvent = new DeviceAlertEvent(eventMessage.deviceId(), deviceOwner.getUsername(), product.id, event.code(), event.value());
                deviceEventBrokerClient.produceDeviceAlert(alertEvent);
            }
        });
    }
}

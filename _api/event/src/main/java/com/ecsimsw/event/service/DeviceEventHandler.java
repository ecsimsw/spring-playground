package com.ecsimsw.event.service;

import com.ecsimsw.common.dto.DeviceAlertEvent;
import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.common.support.client.DeviceClient;
import com.ecsimsw.common.support.client.EventClient;
import com.ecsimsw.event.domain.DeviceAlertEventHistory;
import com.ecsimsw.event.domain.DeviceOwner;
import com.ecsimsw.event.domain.DeviceOwnerRepository;
import com.ecsimsw.event.domain.DeviceStatusEventHistory;
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
    private final DeviceEventHistoryService deviceEventHistoryService;
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

        eventMessage.statuses().forEach(commonDeviceStatus -> {
            var product = PlatformProducts.getById(productId);
            var code = commonDeviceStatus.code();
            var value = commonDeviceStatus.value();
            if (product.isStatusCode(code) || product.isHistoryCode(code)) {
                handleStatusEvent(eventMessage, deviceOwner, code, value);
            }
            if (product.isAlertCode(code)) {
                handleAlertEvent(eventMessage, deviceOwner, code, value);
            }
        });
    }

    private void handleStatusEvent(DeviceEventMessage eventMessage, DeviceOwner deviceOwner, String code, Object value) {
        log.info("Handle device status : {} {}", deviceOwner.getDeviceId(), code);

        var statusEvent = new DeviceStatusEvent(eventMessage.deviceId(), code, value);
        deviceEventBrokerClient.produceDeviceStatus(statusEvent);

        var statusHistory = new DeviceStatusEventHistory(statusEvent.getDeviceId(), code, value);
        deviceEventHistoryService.save(statusHistory);
    }

    private void handleAlertEvent(DeviceEventMessage eventMessage, DeviceOwner deviceOwner, String code, Object value) {
        log.info("Handle device alert : {} {}", deviceOwner.getDeviceId(), code);

        var alertEvent = new DeviceAlertEvent(eventMessage.deviceId(), deviceOwner.getUsername(), code, value);
        deviceEventBrokerClient.produceDeviceAlert(alertEvent);

        var alertHistory = new DeviceAlertEventHistory(alertEvent.getDeviceId(), code, value);
        deviceEventHistoryService.save(alertHistory);
    }
}

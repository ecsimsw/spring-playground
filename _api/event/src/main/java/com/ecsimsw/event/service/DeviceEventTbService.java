package com.ecsimsw.event.service;

import com.ecsimsw.common.domain.Products;
import com.ecsimsw.common.dto.DeviceAlertEvent;
import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.event.domain.DeviceAlertHistory;
import com.ecsimsw.event.domain.DeviceOwner;
import com.ecsimsw.event.domain.DeviceOwnerRepository;
import com.ecsimsw.event.domain.DeviceStatusHistory;
import com.ecsimsw.event.support.DeviceEventBrokerClient;
import com.ecsimsw.sdktb.dto.DeviceEventMessage;
import com.ecsimsw.sdktb.dto.DeviceStatus;
import com.ecsimsw.sdktb.service.PlatformTbEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceEventTbService implements PlatformTbEventService {

    private final DeviceOwnerRepository deviceOwnerRepository;
    private final DeviceEventBrokerClient deviceEventBrokerClient;
    private final DeviceEventHistoryService deviceEventHistoryService;

    public void handle(DeviceEventMessage eventMessage) {
        var productId = eventMessage.productId();
        if (!Products.isSupported(productId)) {
            return;
        }

        var optDeviceOwner = deviceOwnerRepository.findById(eventMessage.deviceId());
        if (optDeviceOwner.isEmpty()) {
            return;
        }

        var deviceOwner = optDeviceOwner.get();
        log.info("Handle device : {} {}", deviceOwner.getDeviceId(), eventMessage.statuses());

        eventMessage.statuses().forEach(statusMap -> {
            var code = statusMap.getCode();
            var value = statusMap.getValue();
            if (deviceOwner.hasStatusCode(code)) {
                handleStatusEvent(eventMessage, deviceOwner, code, value);
            }
            if (deviceOwner.hasAlertCode(code)) {
                handleAlertEvent(eventMessage, deviceOwner, code, value);
            }
        });
    }

    private void handleStatusEvent(DeviceEventMessage eventMessage, DeviceOwner deviceOwner, String code, Object value) {
        log.info("Handle device status : {} {}", deviceOwner.getDeviceId(), code);

        var statusEvent = new DeviceStatusEvent(eventMessage.deviceId(), code, value);
        deviceEventBrokerClient.produceDeviceStatus(statusEvent);

        var statusHistory = new DeviceStatusHistory(statusEvent.getDeviceId(), code, value);
        deviceEventHistoryService.save(statusHistory);
    }

    private void handleAlertEvent(DeviceEventMessage eventMessage, DeviceOwner deviceOwner, String code, Object value) {
        log.info("Handle device alert : {} {}", deviceOwner.getDeviceId(), code);

        var alertEvent = new DeviceAlertEvent(eventMessage.deviceId(), deviceOwner.getUsername(), code, value);
        deviceEventBrokerClient.produceDeviceAlert(alertEvent);

        var alertHistory = new DeviceAlertHistory(alertEvent.getDeviceId(), code, value);
        deviceEventHistoryService.save(alertHistory);
    }
}

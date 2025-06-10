package com.ecsimsw.event.service;

import com.ecsimsw.common.domain.Products;
import com.ecsimsw.common.dto.DeviceAlertEvent;
import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.event.domain.DeviceAlertHistory;
import com.ecsimsw.event.domain.DeviceOwner;
import com.ecsimsw.event.domain.DeviceStatusHistory;
import com.ecsimsw.event.domain.DeviceOwnerRepository;
import com.ecsimsw.sdkty.dto.DeviceEventMessage;
import com.ecsimsw.sdkty.service.PlatformTyEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceEventHandlerServiceService implements PlatformTyEventService {

    private final DeviceOwnerRepository deviceOwnerRepository;
    private final DeviceEventBrokerClient deviceEventBrokerClient;
    private final DeviceEventHistoryService deviceEventHistoryService;

    public void handle(DeviceEventMessage eventMessage) {
        var productId = eventMessage.productId();
        if(!Products.isSupported(productId)) {
            return;
        }

        DeviceOwner deviceOwnerd = null;
        if(eventMessage.deviceId().equals("s8616242a58d13cc66xszg")) {
            deviceOwnerd = new DeviceOwner(
                "s8616242a58d13cc66xszg",
                "username",
                Products.getById("uxjr57hvapakd0io")
            );
        }

        if(deviceOwnerd == null) {
            return;
        }

//        var optDeviceOwner = deviceOwnerRepository.findById(eventMessage.deviceId());
//        if (optDeviceOwner.isEmpty()) {
//            return;
//        }
//
//        var deviceOwner = optDeviceOwner.get();
        var deviceOwner = deviceOwnerd;
        log.info("Handle device : {} {}", deviceOwner.getDeviceId(), eventMessage.statuses());

        eventMessage.statuses().forEach(statusMap -> {
            var deviceType = deviceOwner.getProduct();
            var code = statusMap.getCode();
            var value = statusMap.getValue();
            if (deviceType.hasStatusCode(code)) {
                log.info("Handle device status : {} {}", deviceOwner.getDeviceId(), code);

                var statusEvent = new DeviceStatusEvent(eventMessage.deviceId(), code, value);
                deviceEventBrokerClient.produceDeviceStatus(statusEvent);

                var statusHistory = new DeviceStatusHistory(statusEvent.getDeviceId(), code, value);
                deviceEventHistoryService.save(statusHistory);
            }

            if (deviceType.hasAlertCode(code)) {
                log.info("Handle device alert : {} {}", deviceOwner.getDeviceId(), code);

                var alertEvent = new DeviceAlertEvent(eventMessage.deviceId(), deviceOwner.getUsername(), code, value);
                deviceEventBrokerClient.produceDeviceAlert(alertEvent);

                var alertHistory = new DeviceAlertHistory(alertEvent.getDeviceId(), code, value);
                deviceEventHistoryService.save(alertHistory);
            }
        });
    }
}

package com.ecsimsw.event.service;

import com.ecsimsw.common.dto.DeviceAlertEvent;
import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.event.domain.DeviceAlertHistory;
import com.ecsimsw.event.domain.DeviceStatusHistory;
import com.ecsimsw.event.domain.DeviceOwnerRepository;
import com.ecsimsw.event.dto.DeviceEventMessage;
import com.ecsimsw.springsdkexternalplatform.domain.Products;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventService {

    private final DeviceOwnerRepository deviceOwnerRepository;
    private final DeviceEventBrokerClient deviceEventBrokerClient;
    private final DeviceEventHistoryService deviceEventHistoryService;

    public void handle(DeviceEventMessage eventMessage) {
        var productId = eventMessage.productKey();
        if(!Products.isSupported(productId)) {
            return;
        }

        var optDeviceOwner = deviceOwnerRepository.findById(eventMessage.devId());
        if (optDeviceOwner.isEmpty()) {
            return;
        }

        var deviceOwner = optDeviceOwner.get();
        log.info("Handle device : {} {}", deviceOwner.getDeviceId(), eventMessage.status());

        eventMessage.status().forEach(statusMap -> {
            var deviceType = deviceOwner.getProduct();
            var code = deviceType.parseCode((String) statusMap.get("code"));
            var value = statusMap.get("value");
            if (deviceType.isStatusCode(code)) {
                log.info("Handle device status : {} {}", deviceOwner.getDeviceId(), code);

                var statusEvent = new DeviceStatusEvent(eventMessage.devId(), code, value);
                deviceEventBrokerClient.produceDeviceStatus(statusEvent);

                var statusHistory = new DeviceStatusHistory(statusEvent.getDeviceId(), code, value);
                deviceEventHistoryService.save(statusHistory);
            }

            if (deviceType.isAlertCode(code)) {
                log.info("Handle device alert : {} {}", deviceOwner.getDeviceId(), code);

                var alertEvent = new DeviceAlertEvent(eventMessage.devId(), deviceOwner.getUsername(), code, value);
                deviceEventBrokerClient.produceDeviceAlert(alertEvent);

                var alertHistory = new DeviceAlertHistory(alertEvent.getDeviceId(), code, value);
                deviceEventHistoryService.save(alertHistory);
            }
        });
    }
}

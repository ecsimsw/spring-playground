package com.ecsimsw.event.service;

import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.event.domain.DeviceOwnerRepository;
import com.ecsimsw.event.domain.DeviceStatusHistory;
import com.ecsimsw.event.dto.DeviceStatusEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventService {

    private final DeviceOwnerRepository deviceOwnerRepository;
    private final DeviceStatusEventBrokerClient deviceStatusEventBrokerClient;
    private final DeviceStatusEventHistoryService deviceStatusEventHistoryService;

    public void handle(DeviceStatusEventMessage deviceStatus) {
        var optDeviceOwner = deviceOwnerRepository.findById(deviceStatus.devId());
        if (optDeviceOwner.isEmpty()) {
            return;
        }

        var deviceOwner = optDeviceOwner.get();
        log.info("Handle device : {}", deviceOwner.getDeviceId());

        deviceStatus.status().stream()
            .filter(statusMap -> {
                var code = (String) statusMap.get("code");
                var deviceType = deviceOwner.getProduct();
                return deviceType.isSupportedStatusCode(code);
            })
            .forEach(statusMap -> {

                var code = (String) statusMap.get("code");
                var value = statusMap.get("value");
                log.info("Handle status : {} {}", deviceOwner.getDeviceId(), value);

                var statusEvent = new DeviceStatusEvent(deviceStatus.devId(), code, value);
                deviceStatusEventBrokerClient.produceDeviceStatus(statusEvent);

                var statusHistory = new DeviceStatusHistory(statusEvent.getDeviceId(), code, value);
                deviceStatusEventHistoryService.save(statusHistory);
            });
    }
}

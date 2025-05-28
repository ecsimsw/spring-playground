package com.ecsimsw.event.service;

import com.ecsimsw.common.domain.DeviceType;
import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.event.domain.DeviceOwner;
import com.ecsimsw.event.domain.DeviceOwnerRepository;
import com.ecsimsw.event.domain.DeviceStatusHistory;
import com.ecsimsw.event.dto.DeviceStatusEventMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataEventService {

    private final DeviceOwnerRepository deviceOwnerRepository;
    private final DeviceStatusEventBrokerClient deviceStatusEventBrokerClient;
    private final DeviceStatusEventHistoryService deviceStatusEventHistoryService;

    @PostConstruct
    public void init() {
        deviceOwnerRepository.save(new DeviceOwner(
            "s8616242a58d13cc66xszg",
            "82-00001028088912",
            "uxjr57hvapakd0io",
            DeviceType.Plug
        ));
    }

    public void handle(DeviceStatusEventMessage deviceStatus) {
        var optDeviceOwner = deviceOwnerRepository.findById(deviceStatus.devId());
        if (optDeviceOwner.isEmpty()) {
            return;
        }

        var deviceOwner = optDeviceOwner.get();
        log.info("Handle status : {}", deviceOwner.getDeviceId());

        deviceStatus.status().stream()
            .filter(statusMap -> {
                var code = (String) statusMap.get("code");
                var deviceType = deviceOwner.getDeviceType();
                return deviceType.isSupportedStatusCode(code);
            })
            .forEach(statusMap -> {
                var code = (String) statusMap.get("code");
                var value = statusMap.get("value");

                var statusEvent = new DeviceStatusEvent(deviceStatus.devId(), code, value);
                deviceStatusEventBrokerClient.produceDeviceStatus(statusEvent);

                var statusHistory = new DeviceStatusHistory(statusEvent.getDeviceId(), code, value);
                deviceStatusEventHistoryService.save(statusHistory);
            });
    }
}

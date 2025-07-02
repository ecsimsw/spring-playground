package com.ecsimsw.device.service;

import com.ecsimsw.common.dto.DeviceHistoryEvent;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.device.domain.DeviceHistory;
import com.ecsimsw.device.domain.DeviceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeviceHistoryService {

    private final DeviceHistoryRepository deviceHistoryRepository;
    private final BindDeviceRepository bindDeviceRepository;

    @Transactional
    public void save(DeviceHistoryEvent event) {
        var deviceId = event.getDeviceId();
        var optBindDevice = bindDeviceRepository.findById(deviceId);
        if (optBindDevice.isEmpty()) {
            return;
        }
        var deviceHistory = new DeviceHistory(deviceId, event.statusAsMap(), event.getTimestamp());
        deviceHistoryRepository.save(deviceHistory);
    }
}

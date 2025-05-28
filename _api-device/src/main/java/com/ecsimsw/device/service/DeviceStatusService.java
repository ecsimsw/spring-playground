package com.ecsimsw.device.service;

import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.domain.DeviceStatusRepository;
import com.ecsimsw.device.error.DeviceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeviceStatusService {

    private final DeviceStatusRepository deviceStatusRepository;

    @Transactional
    public void updateStatus(DeviceStatusEvent statusEvent) {
        var deviceId = statusEvent.getDeviceId();
        var deviceStatus = deviceStatusRepository.findByDeviceId(deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.INVALID_DEVICE));
        deviceStatus.updateStatus(statusEvent.getCode(), statusEvent.getValue());
        deviceStatusRepository.save(deviceStatus);
    }
}

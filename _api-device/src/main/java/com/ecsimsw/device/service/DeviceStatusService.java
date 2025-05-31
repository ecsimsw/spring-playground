package com.ecsimsw.device.service;

import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.device.domain.DeviceStatusRepository;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.error.DeviceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceStatusService {

    private final DeviceStatusRepository deviceStatusRepository;
    private final BindDeviceRepository bindDeviceRepository;

    @Transactional
    public void updateStatus(DeviceStatusEvent statusEvent) {
        var deviceId = statusEvent.getDeviceId();
        var optDeviceStatus = deviceStatusRepository.findByDeviceId(deviceId);
        if(optDeviceStatus.isEmpty()) {
            log.info("Enter update status but device not found : {} ", statusEvent.getDeviceId());
            return;
        }
        var deviceStatus = optDeviceStatus.orElseThrow(() -> new DeviceException(ErrorType.INVALID_DEVICE));
        var product = deviceStatus.getProduct();
        if(!product.isSupportedStatusCode(statusEvent.getCode())) {
            log.info("Enter update status but status not supported : {} ", statusEvent.getDeviceId());
            return;
        }
        deviceStatus.updateStatus(statusEvent.getCode(), statusEvent.getValue());
        deviceStatusRepository.save(deviceStatus);
    }

    @Transactional(readOnly = true)
    public DeviceInfoResponse readStatus(String deviceId) {
        var bindDevice = bindDeviceRepository.findById(deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.INVALID_DEVICE));
        var deviceStatus = deviceStatusRepository.findByDeviceId(deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.INVALID_DEVICE));
        return DeviceInfoResponse.of(bindDevice, deviceStatus.getStatus());
    }
}

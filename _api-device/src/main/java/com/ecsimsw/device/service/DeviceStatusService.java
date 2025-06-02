package com.ecsimsw.device.service;

import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.device.domain.DeviceStatusRepository;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.error.DeviceException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final DeviceEventWebSocketService deviceEventWebSocketService;
    private final ObjectMapper objectMapper;

    @Transactional
    public void updateStatus(String message) {
        var deviceStatusEvent = convertFromJson(message);
        updateStatus(deviceStatusEvent);
    }

    @Transactional
    public void updateStatus(DeviceStatusEvent statusEvent) {
        var deviceId = statusEvent.getDeviceId();
        var optDeviceStatus = deviceStatusRepository.findByDeviceId(deviceId);
        if (optDeviceStatus.isEmpty()) {
            log.info("Enter update status but device not found : {} ", statusEvent.getDeviceId());
            return;
        }
        var deviceStatus = optDeviceStatus.orElseThrow();
        var product = deviceStatus.getProduct();
        if (!product.isStatusCode(statusEvent.getCode())) {
            log.info("Enter update status but status not supported : {} ", statusEvent.getDeviceId());
            return;
        }
        deviceStatus.updateStatus(statusEvent.getCode(), statusEvent.getValue());
        deviceStatusRepository.save(deviceStatus);
    }

    // TODO :: Refactor
    public void sendSocket(String message) {
        var statusEvent = convertFromJson(message);
        var optBindDevice = bindDeviceRepository.findById(statusEvent.getDeviceId());
        if (optBindDevice.isEmpty()) {
            return;
        }
        var bindDevice = optBindDevice.orElseThrow(() -> new DeviceException(ErrorType.INVALID_DEVICE));
        deviceEventWebSocketService.sendMessage(bindDevice.getUsername(), message);
    }

    @Transactional(readOnly = true)
    public DeviceInfoResponse readStatus(String username, String deviceId) {
        var bindDevice = bindDeviceRepository.findByUsernameAndDeviceId(username, deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.FORBIDDEN));
        var deviceStatus = deviceStatusRepository.findByDeviceId(deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.INVALID_DEVICE));
        return DeviceInfoResponse.of(bindDevice, deviceStatus.getStatus());
    }

    private DeviceStatusEvent convertFromJson(String statusEvent) {
        try {
            return objectMapper.readValue(statusEvent, DeviceStatusEvent.class);
        } catch (Exception e) {
            log.error("Failed to parse json");
            throw new IllegalArgumentException(e);
        }
    }
}

package com.ecsimsw.device.service;

import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.device.domain.DeviceStatus;
import com.ecsimsw.device.domain.DeviceStatusRepository;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.error.DeviceException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

    @Transactional
    public void updateStatus(DeviceStatusEvent statusEvent) {
        var deviceId = statusEvent.getDeviceId();
        var optBindDevice = bindDeviceRepository.findById(deviceId);
        if (optBindDevice.isEmpty()) {
            return;
        }
        var savedStatus = deviceStatusRepository.findByDeviceId(deviceId);
        if (savedStatus.isEmpty()) {
            var productType = optBindDevice.get().getProduct();
            var status = new DeviceStatus(deviceId, productType, statusEvent.statusAsMap());
            deviceStatusRepository.save(status);
            return;
        }
        var oldStatus = savedStatus.get();
        oldStatus.updateStatus(statusEvent.getCode(), statusEvent.getValue());
        deviceStatusRepository.save(oldStatus);
    }

    @Transactional(readOnly = true)
    public DeviceInfoResponse readStatus(String username, String deviceId) {
        var bindDevice = bindDeviceRepository.findByUsernameAndDeviceId(username, deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.FORBIDDEN));
        var deviceStatus = deviceStatusRepository.findByDeviceId(deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.INVALID_DEVICE));
        return DeviceInfoResponse.of(bindDevice, deviceStatus.getStatus());
    }

    @SneakyThrows
    public void sendSocket(DeviceStatusEvent statusEvent) {
        var optBindDevice = bindDeviceRepository.findById(statusEvent.getDeviceId());
        optBindDevice.ifPresent(
            bindDevice -> deviceEventWebSocketService.sendMessage(bindDevice.getUsername(), statusEvent)
        );
    }
}

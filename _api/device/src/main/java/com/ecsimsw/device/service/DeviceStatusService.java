package com.ecsimsw.device.service;

import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.domain.BindDeviceRepository;
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

    private final BindDeviceRepository bindDeviceRepository;
    private final DeviceEventWebSocketService deviceEventWebSocketService;

    @Transactional
    public void updateStatus(DeviceStatusEvent statusEvent) {
        var deviceId = statusEvent.getDeviceId();
        var optBindDevice = bindDeviceRepository.findById(deviceId);
        if (optBindDevice.isEmpty()) {
            return;
        }
        var bindDevice = optBindDevice.orElseThrow();
        bindDevice.addStatus(statusEvent.getCode(), statusEvent.getValue());
        bindDeviceRepository.save(bindDevice);
    }

    @Transactional(readOnly = true)
    public DeviceInfoResponse readStatus(String username, String deviceId) {
        var bindDevice = bindDeviceRepository.findByUsernameAndDeviceId(username, deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.FORBIDDEN));
        return DeviceInfoResponse.of(bindDevice);
    }

    @SneakyThrows
    public void sendSocket(DeviceStatusEvent statusEvent) {
        var optBindDevice = bindDeviceRepository.findById(statusEvent.getDeviceId());
        optBindDevice.ifPresent(
            bindDevice -> deviceEventWebSocketService.sendStatus(bindDevice.getUsername(), statusEvent.updateEvent())
        );
    }
}

package com.ecsimsw.device.service;

import com.ecsimsw.device.domain.BindDevice;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.springsdkexternalplatform.service.ExternalPlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeviceService {

    private final ExternalPlatformService externalPlatformService;
    private final BindDeviceService bindDeviceService;

    public void deviceList() {

    }

    public void refresh(String username) {
        var userId = externalPlatformService.getUserIdByUsername(username);
        var devices = externalPlatformService.getDevices(userId).stream()
            .map(device -> new BindDevice(
                device.getId(),
                username,
                device.getPid()
            )).toList();
        bindDeviceService.updateAll(username, devices);
    }
}

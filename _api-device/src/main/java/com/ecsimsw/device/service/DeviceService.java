package com.ecsimsw.device.service;

import com.ecsimsw.device.domain.BindDevice;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DeviceService {

    private final BindDeviceRepository bindDeviceRepository;

    public List<String> getDeviceIds(String username) {
        return bindDeviceRepository.findAllByUsername(username).stream()
            .map(BindDevice::getId)
            .toList();
    }

    @Transactional
    public void updateDeviceList(String uid, String username, List<DeviceInfo> devices) {
        var devicesOld = new HashSet<>(bindDeviceRepository.findAllByUsername(username));
        var devicesNew = devices.stream()
            .map(device -> new BindDevice(device.getId(), uid, username, device.getPid()))
            .toList();
        var already = devicesNew.retainAll(devicesOld);
        var newPaired = devicesNew.removeAll(devicesOld);

        bindDeviceRepository.saveAll(bindDevices);
    }
}

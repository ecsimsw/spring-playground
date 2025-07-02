package com.ecsimsw.device.service;

import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.domain.BindDevice;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.error.DeviceException;
import com.ecsimsw.sdkcommon.dto.CommonDeviceStatus;
import com.ecsimsw.sdkcommon.dto.api.DeviceListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DeviceBindService {

    private final BindDeviceRepository bindDeviceRepository;

    @Transactional(readOnly = true)
    public List<DeviceInfoResponse> deviceList(String username) {
        var bindDevices = bindDeviceRepository.findAllByUsername(username);
        return bindDevices.stream()
            .map(DeviceInfoResponse::of)
            .toList();
    }

    @Transactional
    public void deleteAndSaveAll(String username, List<DeviceListResponse> deviceList) {
        bindDeviceRepository.deleteAllByUsername(username);
        var bindDevices = deviceList.stream()
            .map(device -> {
                var deviceStatus = device.status().stream()
                    .collect(Collectors.toMap(
                        CommonDeviceStatus::code,
                        CommonDeviceStatus::value
                    ));
                return new BindDevice(device.id(), username, device.online(), device.name(), device.productId(), deviceStatus);
            }).toList();
        bindDeviceRepository.saveAll(bindDevices);
    }

    @Transactional
    public DeviceInfoResponse getUserDevice(String username, String deviceId) {
        var device = bindDeviceRepository.findByUsernameAndDeviceId(username, deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.FORBIDDEN, "Not device owner"));
        return DeviceInfoResponse.of(device);
    }
}

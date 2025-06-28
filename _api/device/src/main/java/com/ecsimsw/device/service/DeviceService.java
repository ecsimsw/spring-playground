package com.ecsimsw.device.service;

import com.ecsimsw.common.domain.Products;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.domain.BindDevice;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.device.domain.DeviceStatus;
import com.ecsimsw.device.domain.DeviceStatusRepository;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.error.DeviceException;
import com.ecsimsw.sdkcommon.dto.CommonDeviceStatus;
import com.ecsimsw.sdkcommon.dto.api.DeviceListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DeviceService {

    private final BindDeviceRepository bindDeviceRepository;
    private final DeviceStatusRepository deviceStatusRepository;

    @Transactional(readOnly = true)
    public List<DeviceInfoResponse> deviceList(String username) {
        var bindDevices = bindDeviceRepository.findAllByUsername(username);
        var deviceIds = bindDevices.stream()
            .map(BindDevice::getDeviceId)
            .toList();
        var deviceStatusMap = deviceStatusRepository.findAllByDeviceIdIn(deviceIds).stream()
            .collect(Collectors.toMap(
                DeviceStatus::getDeviceId,
                DeviceStatus::getStatus
            ));
        return bindDevices.stream()
            .map(device -> {
                var deviceStatus = deviceStatusMap.get(device.getDeviceId());
                return DeviceInfoResponse.of(device, deviceStatus);
            }).toList();
    }

    @Transactional
    public void deleteAndSaveAll(String username, List<DeviceListResponse> deviceList) {
        bindDeviceRepository.deleteAllByUsername(username);
        var deviceIds = deviceList.stream()
            .map(DeviceListResponse::id)
            .toList();
        deviceStatusRepository.deleteAllByDeviceIdIn(deviceIds);
        bindDevices(username, deviceList);
    }

    @Transactional
    public void bindDevices(String username, List<DeviceListResponse> deviceList) {
        var bindDevices = deviceList.stream()
            .filter(deviceResult -> Products.isSupported(deviceResult.productId()))
            .map(deviceResult -> new BindDevice(
                deviceResult.id(),
                username,
                deviceResult.productId(),
                deviceResult.name(),
                deviceResult.online()
            )).toList();
        bindDeviceRepository.saveAll(bindDevices);

        var updatedStatus = deviceList.stream()
            .filter(deviceInfo -> Products.isSupported(deviceInfo.productId()))
            .map(device -> {
                var product = Products.getById(device.productId());
                var deviceStatus = device.status().stream()
                    .filter(status -> product.hasStatusCode(status.code()))
                    .collect(Collectors.toMap(
                        CommonDeviceStatus::code,
                        statusCode -> product.parseValue(statusCode.code(), statusCode.value())
                    ));
                return new DeviceStatus(device.id(), product, deviceStatus);
            }).toList();
        deviceStatusRepository.saveAll(updatedStatus);
    }

    public DeviceInfoResponse getUserDevice(String username, String deviceId) {
        var device = bindDeviceRepository.findByUsernameAndDeviceId(username, deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.FORBIDDEN, "Not device owner"));
        return DeviceInfoResponse.of(device);
    }
}

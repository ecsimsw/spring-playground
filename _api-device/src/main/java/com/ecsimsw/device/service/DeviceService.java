package com.ecsimsw.device.service;

import com.ecsimsw.device.domain.*;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceInfo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
        var deviceIds = bindDevices.stream().map(BindDevice::getId).toList();
        var deviceStatusMap = deviceStatusRepository.findAllByDeviceIdIn(deviceIds).stream()
            .collect(Collectors.toMap(
                DeviceStatus::getDeviceId,
                DeviceStatus::getStatus)
            );
        return bindDevices.stream()
            .map(device -> new DeviceInfoResponse(
                device.getId(),
                device.getProductId(),
                device.isOnline(),
                deviceStatusMap.get(device.getId())
            )).toList();
    }

    @Transactional
    public void refresh(String username, List<DeviceInfo> deviceResults) {
        updateBindDevice(username, deviceResults);
        updateDeviceStatus(deviceResults);
    }

    private void updateBindDevice(String username, List<DeviceInfo> deviceResults) {
        var bindDevices = deviceResults.stream()
            .filter(deviceResult -> DeviceType.isSupportedProduct(deviceResult.getPid()))
            .map(deviceResult -> new BindDevice(
                deviceResult.getId(),
                username,
                deviceResult.getPid(),
                deviceResult.isOnline()
            )).toList();
        bindDeviceRepository.deleteAllByUsername(username);
        bindDeviceRepository.saveAll(bindDevices);
    }

    @SneakyThrows
    private void updateDeviceStatus(List<DeviceInfo> deviceResults) {
        var deviceIds = deviceResults.stream()
            .map(DeviceInfo::getId)
            .toList();
        deviceStatusRepository.deleteAllByDeviceIdIn(deviceIds);

        var updatedStatus = deviceResults.stream()
            .filter(deviceResult -> DeviceType.isSupportedProduct(deviceResult.getPid()))
            .map(deviceResult -> {
                var deviceType = DeviceType.resolveByProductId(deviceResult.getPid());
                var deviceStatus = deviceResult.getStatus().stream()
                    .filter(status -> deviceType.isSupportedStatusCode(status.getCode()))
                    .collect(Collectors.toMap(
                        it -> it.getCode(),
                        it -> deviceType.getDeviceStatusCode(it.getCode())
                            .asValue(String.valueOf(it.getValue()))
                    ));
                return new DeviceStatus(deviceResult.getId(), deviceStatus);
            }).toList();
        deviceStatusRepository.saveAll(updatedStatus);
    }
}

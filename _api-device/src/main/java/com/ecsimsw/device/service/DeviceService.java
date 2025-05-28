package com.ecsimsw.device.service;

import com.ecsimsw.common.domain.DeviceType;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.domain.*;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.error.DeviceException;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceInfo;
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
        var deviceIds = bindDevices.stream().map(BindDevice::getDeviceId).toList();
        var deviceStatusMap = deviceStatusRepository.findAllByDeviceIdIn(deviceIds).stream()
            .collect(Collectors.toMap(
                DeviceStatus::getDeviceId,
                DeviceStatus::getStatus)
            );
        return bindDevices.stream()
            .map(device -> new DeviceInfoResponse(
                device.getDeviceId(),
                device.getProductId(),
                device.isOnline(),
                deviceStatusMap.get(device.getDeviceId())
            )).toList();
    }

    @Transactional
    public void refresh(String username, List<DeviceInfo> deviceResults) {
        bindDeviceRepository.deleteAllByUsername(username);
        var deviceIds = deviceResults.stream()
            .map(DeviceInfo::getId)
            .toList();
        deviceStatusRepository.deleteAllByDeviceIdIn(deviceIds);

        bindDevices(username, deviceResults);
    }

    @Transactional
    public void bindDevices(String username, List<DeviceInfo> deviceInfos) {
        var bindDevices = deviceInfos.stream()
            .filter(deviceResult -> DeviceType.isSupportedProduct(deviceResult.getPid()))
            .map(deviceResult -> new BindDevice(
                deviceResult.getId(),
                username,
                deviceResult.getPid(),
                deviceResult.isOnline()
            )).toList();
        bindDeviceRepository.saveAll(bindDevices);

        var updatedStatus = deviceInfos.stream()
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

    @Transactional(readOnly = true)
    public DeviceInfoResponse readStatus(String deviceId) {
        var bindDevice = bindDeviceRepository.findById(deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.INVALID_DEVICE));
        var deviceStatus = deviceStatusRepository.findByDeviceId(deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.INVALID_DEVICE));
        return new DeviceInfoResponse(
            bindDevice.getDeviceId(),
            bindDevice.getProductId(),
            bindDevice.isOnline(),
            deviceStatus.getStatus()
        );
    }
}

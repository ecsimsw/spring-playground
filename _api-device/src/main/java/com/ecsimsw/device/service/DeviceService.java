package com.ecsimsw.device.service;

import com.ecsimsw.common.domain.DeviceType;
import com.ecsimsw.device.domain.BindDevice;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.device.domain.DeviceStatus;
import com.ecsimsw.device.domain.DeviceStatusRepository;
import com.ecsimsw.device.dto.DeviceInfoResponse;
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
            .map(device -> {
                var deviceType = device.getType();
                var deviceStatus = deviceStatusMap.get(device.getDeviceId());
                var parsedStatusMap = deviceStatus.keySet().stream()
                    .filter(deviceType::isSupportedStatusCode)
                    .collect(Collectors.toMap(statusCode -> statusCode, deviceStatus::get));
                return new DeviceInfoResponse(
                    device.getDeviceId(),
                    device.getProductId(),
                    device.isOnline(),
                    parsedStatusMap
                );
            }).toList();
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
            .filter(deviceInfo -> DeviceType.isSupportedProduct(deviceInfo.getPid()))
            .map(this::deviceInfoToDeviceStatus)
            .toList();
        deviceStatusRepository.saveAll(updatedStatus);
    }

    private DeviceStatus deviceInfoToDeviceStatus(DeviceInfo deviceInfo) {
        var deviceType = DeviceType.resolveByProductId(deviceInfo.getPid());
        var deviceStatus = deviceInfo.getStatus().stream()
            .filter(status -> deviceType.isSupportedStatusCode(status.getCode()))
            .collect(Collectors.toMap(
                statusCode -> statusCode.getCode(),
                statusCode -> deviceType.convertValue(statusCode.getCode(), statusCode.getValue())
            ));
        return new DeviceStatus(deviceInfo.getId(), deviceType, deviceStatus);
    }
}

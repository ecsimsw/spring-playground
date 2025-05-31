package com.ecsimsw.device.service;

import com.ecsimsw.common.domain.Products;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.domain.BindDevice;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.device.domain.DeviceStatus;
import com.ecsimsw.device.domain.DeviceStatusRepository;
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
        var deviceIds = bindDevices.stream()
            .map(BindDevice::getDeviceId)
            .toList();
        var deviceStatusMap = deviceStatusRepository.findAllByDeviceIdIn(deviceIds).stream()
            .collect(Collectors.toMap(
                DeviceStatus::getDeviceId,
                DeviceStatus::getStatus)
            );
        return bindDevices.stream()
            .map(device -> DeviceInfoResponse.of(
                device,
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
            .filter(deviceResult -> Products.isSupported(deviceResult.getPid()))
            .map(deviceResult -> new BindDevice(
                deviceResult.getId(),
                username,
                deviceResult.getPid(),
                deviceResult.getName(),
                deviceResult.isOnline()
            )).toList();
        bindDeviceRepository.saveAll(bindDevices);

        var updatedStatus = deviceInfos.stream()
            .filter(deviceInfo -> Products.isSupported(deviceInfo.getPid()))
            .map(this::deviceInfoToDeviceStatus)
            .toList();
        deviceStatusRepository.saveAll(updatedStatus);
    }

    private DeviceStatus deviceInfoToDeviceStatus(DeviceInfo deviceInfo) {
        var product = Products.getById(deviceInfo.getPid());
        var deviceStatus = deviceInfo.getStatus().stream()
            .filter(status -> product.isSupportedStatusCode(status.getCode()))
            .collect(Collectors.toMap(
                statusCode -> statusCode.getCode(),
                statusCode -> product.convertValue(statusCode.getCode(), statusCode.getValue())
            ));
        return new DeviceStatus(deviceInfo.getId(), product, deviceStatus);
    }

    public DeviceInfoResponse getUserDevice(String username, String deviceId) {
        var device = bindDeviceRepository.findByUsernameAndDeviceId(username, deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.FORBIDDEN, "Not device owner"));
        return DeviceInfoResponse.of(device);
    }
}

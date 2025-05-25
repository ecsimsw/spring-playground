package com.ecsimsw.device.service;

import com.ecsimsw.device.domain.*;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DeviceService {

    private final ObjectMapper objectMapper;
    private final BindDeviceRepository bindDeviceRepository;
    private final DeviceStatusRepository deviceStatusRepository;

    public List<DeviceInfoResponse> deviceList(String username) {
        return bindDeviceRepository.findAllByUsername(username).stream()
            .map(device -> new DeviceInfoResponse(
                device.getId(),
                device.getName(),
                device.getProductId(),
                new HashMap<>()
            )).toList();
    }

    @SneakyThrows
    @Transactional
    public void refresh(String username, List<DeviceResult> deviceResults) {
        var bindDevices = deviceResults.stream()
            .map(device -> new BindDevice(
                device.getId(),
                username,
                device.getName(),
                device.getPid(),
                device.getOnline()
            )).toList();
        bindDeviceRepository.deleteAllByUsername(username);
        bindDeviceRepository.saveAll(bindDevices);

        for (var device : deviceResults) {
            var deviceId = device.getId();
            var optDeviceType = DeviceType.findTypeByProductId(device.getPid());
            if (optDeviceType.isEmpty()) {
                continue;
            }
            var deviceType = optDeviceType.get();
            var statusCode = deviceType.statusCode();
            var deviceStatusMap = new HashMap<String, Object>();
            for (var status : device.getStatus()) {
                var optCode = statusCode.stream()
                    .filter(it -> it.name().equals(status.getCode()))
                    .findFirst();
                if (optCode.isPresent()) {
                    var deviceStatusCode = optCode.get();
                    var codeName = deviceStatusCode.name();
                    var value = deviceStatusCode.asValue(String.valueOf(status.getValue()));
                    deviceStatusMap.put(codeName, value);
                }
            }
            var jsonStatus = objectMapper.writeValueAsString(deviceStatusMap);
            var deviceStatus = new DeviceStatus(deviceId, jsonStatus);
            deviceStatusRepository.deleteByDeviceId(deviceId);
            deviceStatusRepository.save(deviceStatus);
        }
    }
}

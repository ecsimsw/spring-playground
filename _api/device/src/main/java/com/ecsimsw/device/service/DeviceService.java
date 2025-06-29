package com.ecsimsw.device.service;

//import com.ecsimsw.common.domain.Products;
import com.ecsimsw.common.error.ApiException;
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
public class DeviceService {

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

        var bindDevices = new ArrayList<BindDevice>();
        for(var device : deviceList) {
//            if(!Products.isSupported(device.productId())) {
//                System.out.println(device.productId());
//                continue;
//            }
            try {
//                var product = Products.getById(device.productId());
                var bindDevice = new BindDevice(device.id(), username, device.productId(), device.name(), device.online());
                var deviceStatus = device.status().stream()
                    .collect(Collectors.toMap(
                        CommonDeviceStatus::code,
                        CommonDeviceStatus::value
                    ));
                bindDevice.setStatus(deviceStatus);
                bindDevices.add(bindDevice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        bindDeviceRepository.saveAll(bindDevices);
    }

    @Transactional
    public DeviceInfoResponse getUserDevice(String username, String deviceId) {
        var device = bindDeviceRepository.findByUsernameAndDeviceId(username, deviceId)
            .orElseThrow(() -> new DeviceException(ErrorType.FORBIDDEN, "Not device owner"));
        return DeviceInfoResponse.of(device);
    }
}

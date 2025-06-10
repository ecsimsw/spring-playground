package com.ecsimsw.springsdkexternalplatform.service;

import com.ecsimsw.springsdkexternalplatform.domain.PlatformProducts;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceInfo;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Platform1DeviceApiHandler {

    private final TuyaDeviceApiService tuyaDeviceApiService;
    private final ThingsboardPlatformService thingsboardPlatformService;

    public String getUserIdByUsername(String username) {
        return tuyaDeviceApiService.getUserIdByUsername(username);
    }

    public List<DeviceInfo> getDeviceList(String username) {
        var userId = getUserIdByUsername(username);
        var tuyaDevices = tuyaDeviceApiService.getDeviceListByUserId(userId);
        var mqttDevices = thingsboardPlatformService.getDeviceListByUsername(username);
        tuyaDevices.addAll(mqttDevices);
        return tuyaDevices;
    }

    public void command(String deviceId, String productId, List<DeviceStatus> deviceStatuses) {
        if(PlatformProducts.isSupported(productId)) {
            tuyaDeviceApiService.command(deviceId, productId, deviceStatuses);
            return;
        }
        thingsboardPlatformService.command(deviceId, productId, deviceStatuses);
    }
}

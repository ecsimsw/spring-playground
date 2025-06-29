package com.ecsimsw.event.service;

import com.ecsimsw.common.domain.Products;
import com.ecsimsw.event.domain.DeviceOwner;
import com.ecsimsw.event.domain.DeviceOwnerRepository;
import com.ecsimsw.sdkcommon.dto.api.DeviceListResponse;
import com.ecsimsw.sdkty.dto.TuyaDeviceStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceOwnerService {

    private final DeviceOwnerRepository deviceOwnerRepository;

    @Transactional
    public void updateAll(String username, List<DeviceListResponse> deviceList) {
        deviceOwnerRepository.deleteAllByUsername(username);

        var deviceOwners = deviceList.stream()
            .filter(deviceInfo -> Products.isSupported(deviceInfo.productId()))
            .map(deviceInfo -> new DeviceOwner(
                deviceInfo.id(),
                username,
                Products.getById(deviceInfo.productId())
            )).toList();
        deviceOwnerRepository.saveAll(deviceOwners);
    }
}

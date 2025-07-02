package com.ecsimsw.event.service;

import com.ecsimsw.event.domain.DeviceOwner;
import com.ecsimsw.event.domain.DeviceOwnerRepository;
import com.ecsimsw.sdkcommon.domain.PlatformProducts;
import com.ecsimsw.sdkcommon.dto.api.DeviceListResponse;
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
    public void deleteAndSaveAll(String username, List<DeviceListResponse> deviceList) {
        deviceOwnerRepository.deleteAllByUsername(username);

        var deviceOwners = deviceList.stream()
            .filter(deviceInfo -> PlatformProducts.isSupported(deviceInfo.productId()))
            .map(deviceInfo -> new DeviceOwner(
                deviceInfo.id(),
                username
            )).toList();
        deviceOwnerRepository.saveAll(deviceOwners);
    }
}

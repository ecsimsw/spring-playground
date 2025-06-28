package com.ecsimsw.event.service;

import com.ecsimsw.common.domain.Products;
import com.ecsimsw.event.domain.DeviceOwner;
import com.ecsimsw.event.domain.DeviceOwnerRepository;
import com.ecsimsw.sdkty.dto.DeviceStatusResponse;
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
    public void updateAll(String username, List<DeviceStatusResponse> deviceStatusResponses) {
        deviceOwnerRepository.deleteAllByUsername(username);

        var deviceOwners = deviceStatusResponses.stream()
            .filter(deviceInfo -> Products.isSupported(deviceInfo.getPid()))
            .map(deviceInfo -> new DeviceOwner(
                deviceInfo.getId(),
                username,
                Products.getById(deviceInfo.getPid())
            )).toList();
        deviceOwnerRepository.saveAll(deviceOwners);
    }
}

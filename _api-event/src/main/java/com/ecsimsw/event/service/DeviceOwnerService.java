package com.ecsimsw.event.service;

import com.ecsimsw.common.domain.DeviceType;
import com.ecsimsw.event.domain.DeviceOwner;
import com.ecsimsw.event.domain.DeviceOwnerRepository;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceInfo;
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
    public void refresh(String username, List<DeviceInfo> deviceInfos) {
        deviceOwnerRepository.deleteAllByUsername(username);

        var deviceOwners = deviceInfos.stream()
            .map(deviceInfo -> new DeviceOwner(
                deviceInfo.getId(),
                username,
                deviceInfo.getPid(),
                DeviceType.resolveByProductId(deviceInfo.getPid())
            )).toList();
        deviceOwnerRepository.saveAll(deviceOwners);
    }
}

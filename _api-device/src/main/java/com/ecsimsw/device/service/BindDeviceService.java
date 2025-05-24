package com.ecsimsw.device.service;

import com.ecsimsw.device.domain.BindDevice;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.springsdkexternalplatform.service.ExternalPlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BindDeviceService {

    private final BindDeviceRepository bindDeviceRepository;

    public void deviceList() {

    }

    @Transactional
    public void updateAll(String username, List<BindDevice> bindDevices) {
        bindDeviceRepository.deleteAllByUsername(username);
        bindDeviceRepository.saveAll(bindDevices);
    }
}

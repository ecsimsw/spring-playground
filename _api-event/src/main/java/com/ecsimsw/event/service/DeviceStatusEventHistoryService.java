package com.ecsimsw.event.service;

import com.ecsimsw.event.domain.DeviceStatusHistory;
import com.ecsimsw.event.domain.DeviceStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceStatusEventHistoryService {

    private final DeviceStatusHistoryRepository deviceStatusHistoryRepository;

    public void save(DeviceStatusHistory statusHistory) {
        deviceStatusHistoryRepository.save(statusHistory).subscribe(
            data -> log.info("Save history : {} {} {}", statusHistory.getDeviceId(), statusHistory.getCode(), statusHistory.getValue()),
            error -> log.error("Failed to save history : {}", statusHistory)
        );
    }
}

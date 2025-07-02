package com.ecsimsw.event.service;

import com.ecsimsw.event.domain.DeviceAlertEventHistory;
import com.ecsimsw.event.domain.DeviceAlertEventHistoryRepository;
import com.ecsimsw.event.domain.DeviceStatusEventHistory;
import com.ecsimsw.event.domain.DeviceStatusEventHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceEventHistoryService {

    private final DeviceStatusEventHistoryRepository deviceStatusEventHistoryRepository;
    private final DeviceAlertEventHistoryRepository deviceAlertEventHistoryRepository;

    public void save(DeviceStatusEventHistory statusHistory) {
        deviceStatusEventHistoryRepository.save(statusHistory).subscribe(
            data -> log.info("Save status history : {} {} {}", statusHistory.getDeviceId(), statusHistory.getCode(), statusHistory.getValue()),
            error -> log.error("Failed to save status history : {}", statusHistory)
        );
    }

    public void save(DeviceAlertEventHistory alertHistory) {
        deviceAlertEventHistoryRepository.save(alertHistory).subscribe(
            data -> log.info("Save alert history : {} {} {}", alertHistory.getDeviceId(), alertHistory.getCode(), alertHistory.getValue()),
            error -> log.error("Failed to save alert history : {}", alertHistory)
        );
    }
}

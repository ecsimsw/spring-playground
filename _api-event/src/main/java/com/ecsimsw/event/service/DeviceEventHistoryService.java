package com.ecsimsw.event.service;

import com.ecsimsw.event.domain.DeviceAlertHistory;
import com.ecsimsw.event.domain.DeviceAlertHistoryRepository;
import com.ecsimsw.event.domain.DeviceStatusHistory;
import com.ecsimsw.event.domain.DeviceStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceEventHistoryService {

    private final DeviceStatusHistoryRepository deviceStatusHistoryRepository;
    private final DeviceAlertHistoryRepository deviceAlertHistoryRepository;

    public void save(DeviceStatusHistory statusHistory) {
        deviceStatusHistoryRepository.save(statusHistory).subscribe(
            data -> log.info("Save status history : {} {} {}", statusHistory.getDeviceId(), statusHistory.getCode(), statusHistory.getValue()),
            error -> log.error("Failed to save status history : {}", statusHistory)
        );
    }

    public void save(DeviceAlertHistory alertHistory) {
        deviceAlertHistoryRepository.save(alertHistory).subscribe(
            data -> log.info("Save alert history : {} {} {}", alertHistory.getDeviceId(), alertHistory.getCode(), alertHistory.getValue()),
            error -> log.error("Failed to save alert history : {}", alertHistory)
        );
    }
}

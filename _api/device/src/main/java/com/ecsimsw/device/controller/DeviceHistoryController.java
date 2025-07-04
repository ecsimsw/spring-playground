package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.common.dto.DeviceHistoryEvent;
import com.ecsimsw.common.error.ApiException;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.device.dto.DeviceHistoryPageResponse;
import com.ecsimsw.device.service.DeviceHistoryService;
import com.ecsimsw.device.support.ConverterUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeviceHistoryController {

    private final DeviceHistoryService deviceHistoryService;
    private final BindDeviceRepository bindDeviceRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "${kafka.device.history.topic}",
        groupId = "${kafka.device.history.groupId}",
        concurrency = "${kafka.device.history.partitionCount}"
    )
    public void listenHistory(String message) {
        var statusEvent = convertFromJson(message);
        log.info("Handle device history event {} {}", statusEvent.deviceId(), statusEvent.statusAsMap());
        deviceHistoryService.save(statusEvent);
    }

    @GetMapping("/api/device/{deviceId}/history/{historyCode}")
    public ApiResponse<Mono<DeviceHistoryPageResponse>> getHistoriesCursor(
        @PathVariable String deviceId,
        @PathVariable String historyCode,
        Pageable pageable,
        @RequestParam String cursor,
        AuthUser user
    ) {
        if (!bindDeviceRepository.existsByUsernameAndDeviceId(user.username(), deviceId)) {
            throw new ApiException(ErrorType.INVALID_DEVICE);
        }
        if (ConverterUtils.isStringInteger(cursor)) {
            var numericCursor = Integer.parseInt(cursor);
            var result = deviceHistoryService.historiesByCursor(deviceId, historyCode, pageable.getSort(), numericCursor, pageable.getPageSize());
            return ApiResponse.success(result);
        }
        var result = deviceHistoryService.historiesByCursor(deviceId, historyCode, pageable.getSort(), cursor, pageable.getPageSize());
        return ApiResponse.success(result);
    }

    private DeviceHistoryEvent convertFromJson(String historyEvent) {
        try {
            return objectMapper.readValue(historyEvent, DeviceHistoryEvent.class);
        } catch (Exception e) {
            log.error("Failed to parse json");
            throw new IllegalArgumentException(e);
        }
    }
}

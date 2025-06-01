package com.ecsimsw.event.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DeviceAlertHistoryRepository extends ReactiveMongoRepository<DeviceAlertHistory, String> {
}

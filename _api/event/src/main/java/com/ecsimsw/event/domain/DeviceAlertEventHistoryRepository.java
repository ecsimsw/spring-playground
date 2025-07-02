package com.ecsimsw.event.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DeviceAlertEventHistoryRepository extends ReactiveMongoRepository<DeviceAlertEventHistory, String> {
}

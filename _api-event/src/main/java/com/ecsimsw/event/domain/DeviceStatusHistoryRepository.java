package com.ecsimsw.event.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DeviceStatusHistoryRepository extends ReactiveMongoRepository<DeviceStatusHistory, String> {
}

package com.ecsimsw.event.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DeviceStatusEventHistoryRepository extends ReactiveMongoRepository<DeviceStatusEventHistory, String> {
}

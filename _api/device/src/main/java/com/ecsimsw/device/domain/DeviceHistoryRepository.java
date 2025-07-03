package com.ecsimsw.device.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DeviceHistoryRepository extends ReactiveMongoRepository<DeviceHistory, String> {

}

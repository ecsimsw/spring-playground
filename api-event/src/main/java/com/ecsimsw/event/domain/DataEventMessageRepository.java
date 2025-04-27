package com.ecsimsw.event.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface DataEventMessageRepository extends ReactiveMongoRepository<DataEventMessage, String> {

    Mono<DataEventMessage> findById(String id);
}

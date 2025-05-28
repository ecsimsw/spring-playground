package com.ecsimsw.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventMessageDeadLetterService {

    private static final String DEAD_LETTER_COLLECTION_NAME = "event_message_dead_letter";

    private final ReactiveMongoTemplate mongoTemplate;

    public void save(String failedEventMessage) {
        mongoTemplate.save(failedEventMessage, DEAD_LETTER_COLLECTION_NAME).subscribe(
            data -> log.info("Succeed to save event message dead letter to mongodb : {}", failedEventMessage),
            error -> log.error("Failed to save event message dead letter to mongodb : {}", failedEventMessage)
        );
    }
}

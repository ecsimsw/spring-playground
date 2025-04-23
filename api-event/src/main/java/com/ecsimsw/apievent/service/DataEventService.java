package com.ecsimsw.apievent.service;

import com.ecsimsw.apievent.domain.DataEventMessage;
import com.ecsimsw.apievent.domain.DataEventMessageRepository;
import com.ecsimsw.apievent.domain.EventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataEventService {

    private final DataEventMessageRepository dataEventMessageRepository;

    public void handle(EventMessage eventMessage) {

    }

    public void saveBlocking(DataEventMessage dataEvent) {
        dataEventMessageRepository.save(dataEvent)
            .block();
    }

    public void saveNonBlocking(DataEventMessage dataEvent) {
        dataEventMessageRepository.save(dataEvent)
            .subscribe(
                data -> {},
                error -> log.error("Failed to save : {}", dataEvent)
            );
    }

    public DataEventMessage find(String dataId) {
        var eventMessage =dataEventMessageRepository.findById(dataId).block();
        System.out.println(eventMessage);
        return eventMessage;
    }
}

package com.ecsimsw.apievent.service;

import com.ecsimsw.apievent.domain.DataEventMessage;
import com.ecsimsw.apievent.domain.DataEventMessageRepository;
import com.ecsimsw.common.client.NotificationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataEventService {

    private final DataEventMessageRepository dataEventMessageRepository;
    private final NotificationClient notificationClient;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void handle(DataEventMessage dataEvent) {
        log.info("recv : {}", dataEvent.getDevId());
        notificationClient.createNotificationAsync(dataEvent.getDevId()).subscribe(
            data -> {},
            error -> log.error("Failed to send notification server : {}", dataEvent)
        );
        dataEventMessageRepository.save(dataEvent).subscribe(
            data -> {},
            error -> log.error("Failed to save : {}", dataEvent)
        );
//        kafkaTemplate.send()
    }
}

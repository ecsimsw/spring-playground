package com.ecsimsw.apievent.service;

import com.ecsimsw.apievent.domain.DataEventMessage;
import com.ecsimsw.apievent.domain.DataEventMessageRepository;
import com.ecsimsw.common.client.NotificationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataEventService {

    private final DataEventMessageRepository dataEventMessageRepository;
    private final NotificationClient notificationClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${notification.kafka.topic}")
    private String sampleTopic;

    public void handle(DataEventMessage dataEvent) {
        log.info("\n0thread id : {}\n ", (Thread.currentThread().getId()));
        var startTime = System.currentTimeMillis();

        notificationClient.createNotificationAsync(dataEvent.getDataId()).subscribe(
            data -> {},
            error -> log.info("\n1thread id : {}\n ", (Thread.currentThread().getName()))
        );

        dataEventMessageRepository.save(dataEvent).subscribe(
            data -> log.info("\n2thread id : {}\n ", (Thread.currentThread().getName())),
            error -> log.info("\n2thread id : {}\n ", (Thread.currentThread().getName()))
        );


        kafkaTemplate.send(sampleTopic, dataEvent.getDevId(), dataEvent.getDataId())
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.info("\nexception : {}\n ", (Thread.currentThread().getName()));
                }
                log.info("\n3thread id : {}\n ", (Thread.currentThread().getName()));

            });

        log.info("fin : {}", System.currentTimeMillis() - startTime);
    }
}

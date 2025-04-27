package com.ecsimsw.event.service;

import com.ecsimsw.event.domain.EventMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PulsarConsumerService {

    @Value("${pulsar.event.topic}")
    private String topics;

    @Value("${pulsar.event.subscriptionName}")
    private String subscriptionName;

    private final PulsarClient client;
    private final ObjectMapper objectMapper;

    public void makeOffsetLatest() {
        try {
            Consumer<byte[]> consumer = client.newConsumer(Schema.BYTES)
                .topic(topics)
                .subscriptionName(subscriptionName)
                .subscriptionType(SubscriptionType.Shared)
                .subscribe();
            consumer.seek(MessageId.latest);

            while (true) {
                var message = consumer.receive();
                var eventMessage = EventMessage.from(objectMapper, message);
                log.info("{}", System.currentTimeMillis() - eventMessage.t());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}

package com.ecsimsw.event.controller;

import com.ecsimsw.event.domain.EventMessage;
import com.ecsimsw.event.dto.DeviceStatusEventMessage;
import com.ecsimsw.event.service.EventMessageDeadLetterService;
import com.ecsimsw.event.service.EventService;
import com.ecsimsw.event.support.EventThroughputCounter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
import org.apache.pulsar.client.api.SubscriptionType;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.ecsimsw.event.domain.support.Protocol.DATA;

@Slf4j
@RequiredArgsConstructor
@Controller
public class DeviceEventListener {

    @Value("${pulsar.event.secretKey}")
    private String secretKey;

    @Value("${pulsar.event.topic}")
    private String topic;

    @Value("${pulsar.event.subscriptionName}")
    private String subscriptionName;

    @Value("${pulsar.event.partitionNumber}")
    private int partitionNumber;

    private final ObjectMapper objectMapper;
    private final EventService eventService;
    private final EventThroughputCounter eventThroughputCounter;
    private final PulsarClient pulsarClient;
    private final EventMessageDeadLetterService eventMessageDeadLetterService;

    @PostConstruct
    public void init() {
        eventThroughputCounter.start(1, TimeUnit.SECONDS);
        listen(partitionNumber);
    }

    @SneakyThrows
    public void listen(int consumerCount) {
        var executor = Executors.newFixedThreadPool(consumerCount);
        IntStream.rangeClosed(1, consumerCount).forEach(
            i -> executor.submit(() -> {
                MDC.put("threadId", String.valueOf(Thread.currentThread().threadId()));
                consume();
                MDC.clear();
            })
        );
    }

    @SneakyThrows
    private void consume() {
        var consumer = initConsumer();
        while (!Thread.currentThread().isInterrupted()) {
            var msg = consumer.receive(1, TimeUnit.SECONDS);
            if (msg == null) {
                continue;
            }
            try {
                var eventMessage = EventMessage.from(objectMapper, msg);
                if (eventMessage.isProtocol(DATA)) {
                    var dataEvent = DeviceStatusEventMessage.from(objectMapper, eventMessage, secretKey);
                    eventService.handle(dataEvent);
                }
            } catch (Exception e) {
                eventMessageDeadLetterService.save(new String(msg.getData()));
            } finally {
                consumer.acknowledge(msg);
                eventThroughputCounter.up();
            }
        }
    }

    @SneakyThrows
    private Consumer<byte[]> initConsumer() {
        return pulsarClient.newConsumer()
            .topic("persistent://" + topic)
            .subscriptionName(subscriptionName)
            .subscriptionType(SubscriptionType.Shared)
            .subscriptionInitialPosition(SubscriptionInitialPosition.Latest)
            .subscribe();
    }

    @PreDestroy
    public void destroyCount() {
        eventThroughputCounter.end();
    }
}

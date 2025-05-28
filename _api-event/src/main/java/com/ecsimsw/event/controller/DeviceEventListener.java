package com.ecsimsw.event.controller;

import com.ecsimsw.event.domain.EventMessage;
import com.ecsimsw.event.dto.DeviceStatusEventMessage;
import com.ecsimsw.event.service.DataEventService;
import com.ecsimsw.event.support.EventThroughputCounter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
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

    private final ObjectMapper objectMapper;
    private final DataEventService dataEventService;
    private final EventThroughputCounter eventThroughputCounter;
    private final PulsarClient pulsarClient;

    @Value("${pulsar.event.secretKey}")
    private String secretKey;

    @Value("${pulsar.event.topic}")
    private String topic;

    @Value("${pulsar.event.subscriptionName}")
    private String subscriptionName;

    @Value("${pulsar.event.partitionNumber}")
    private int partitionNumber;

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
                try {
                    MDC.put("threadId", String.valueOf(Thread.currentThread().getId()));
                    consume();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    MDC.clear();
                }
            })
        );
    }

    private void consume() throws PulsarClientException {
        var consumer = pulsarClient.newConsumer()
            .topic("persistent://" + topic)
            .subscriptionName(subscriptionName)
            .subscriptionType(SubscriptionType.Shared)
            .subscriptionInitialPosition(SubscriptionInitialPosition.Latest)
            .subscribe();
        while (!Thread.currentThread().isInterrupted()) {
            var msg = consumer.receive(1, TimeUnit.SECONDS);
            try {
                if (msg != null) {
                    var eventMessage = EventMessage.from(objectMapper, msg);
                    if (eventMessage.isProtocol(DATA)) {
                        var dataEvent = DeviceStatusEventMessage.from(objectMapper, eventMessage, secretKey);
                        dataEventService.handle(dataEvent);
                    }
                    consumer.acknowledge(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO :: 재시도와 DLQ, 예외 처리
            }
            eventThroughputCounter.up();
        }
    }

    @PreDestroy
    public void destroyCount() {
        eventThroughputCounter.end();
    }
}

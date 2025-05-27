package com.ecsimsw.event.controller;

import com.ecsimsw.event.domain.DataEventMessage;
import com.ecsimsw.event.domain.EventMessage;
import com.ecsimsw.event.service.DataEventService;
import com.ecsimsw.event.service.EventThroughputCounter;
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
                    consume(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            })
        );
    }

    private void consume(int consumerId) throws PulsarClientException {
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
                        var dataEvent = DataEventMessage.from(objectMapper, eventMessage, secretKey);
                        dataEventService.handle(eventMessage.t(), dataEvent);
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

//    @PulsarListener(
//        topics = "${pulsar.event.topic}",
//        subscriptionName = "${pulsar.event.subscriptionName}",
//        subscriptionType = SubscriptionType.Shared,
//        concurrency = "11"
//    )
//    public void listen(Message<byte[]> message) {
//        eventThroughputCounter.up();
//        try {
//            MDC.put("threadId", String.valueOf(Thread.currentThread().getId()));
//            var eventMessage = EventMessage.from(objectMapper, message);
//            if (eventMessage.isProtocol(DATA)) {
//                var dataEvent = DataEventMessage.from(objectMapper, eventMessage, secretKey);
//                System.out.println(dataEvent.getDevId());
////                dataEventService.handle(dataEvent);
//            }
//        } finally {
//            MDC.clear();
//        }
//    }

    @PreDestroy
    public void destroyCount() {
        eventThroughputCounter.end();
    }
}

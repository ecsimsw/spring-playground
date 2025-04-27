package com.ecsimsw.event.controller;

import com.ecsimsw.event.domain.DataEventMessage;
import com.ecsimsw.event.domain.EventMessage;
import com.ecsimsw.event.service.EventThroughputCounter;
import com.ecsimsw.event.service.DataEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.SubscriptionType;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Controller;

import static com.ecsimsw.event.domain.support.Protocol.DATA;

@Slf4j
@RequiredArgsConstructor
@Controller
public class DeviceEventListener {

    private final ObjectMapper objectMapper;
    private final DataEventService dataEventService;
    private final EventThroughputCounter eventThroughputCounter;

    @Value("${pulsar.event.secretKey}")
    private String secretKey;

    @PostConstruct
    public void initCount() {
        eventThroughputCounter.startCount();
    }

    @PulsarListener(
        topics = "${pulsar.event.topic}",
        subscriptionName = "${pulsar.event.subscriptionName}",
        subscriptionType = SubscriptionType.Shared,
        concurrency = "11"
    )
    public void listen(Message<byte[]> message) {
        eventThroughputCounter.up();
        try {
            MDC.put("threadId", String.valueOf(Thread.currentThread().getId()));
            var eventMessage = EventMessage.from(objectMapper, message);
            System.out.println(System.currentTimeMillis() - eventMessage.t());
            if (eventMessage.isProtocol(DATA)) {
                var dataEvent = DataEventMessage.from(objectMapper, eventMessage, secretKey);
                dataEventService.handle(dataEvent);
            }
        } finally {
            MDC.clear();
//            TimeUtils.sleep(30_000);
//            TimeUtils.sleep(60_000L);
        }
    }
}

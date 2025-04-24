package com.ecsimsw.apievent.controller;

import com.ecsimsw.apievent.domain.DataEventMessage;
import com.ecsimsw.apievent.domain.EventMessage;
import com.ecsimsw.apievent.domain.StatusEventMessage;
import com.ecsimsw.apievent.service.DataEventService;
import com.ecsimsw.apievent.service.StatusEventService;
import com.ecsimsw.common.support.utils.TimeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.SubscriptionType;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Controller;

import static com.ecsimsw.apievent.domain.support.Protocol.DATA;
import static com.ecsimsw.apievent.domain.support.Protocol.STATUS;

@Slf4j
@RequiredArgsConstructor
@Controller
public class DeviceEventListener {

    private final ObjectMapper objectMapper;
    private final DataEventService dataEventService;
    private final StatusEventService statusEventService;

    @Value("${pulsar.event.secretKey}")
    private String secretKey;

    @PulsarListener(
        topics = "${pulsar.event.topic}",
        subscriptionName = "${pulsar.event.subscriptionName}",
        subscriptionType = SubscriptionType.Shared,
        concurrency = "11"
    )
    public void listen(Message<byte[]> message) {
        try {
            MDC.put("threadId", String.valueOf(Thread.currentThread().getId()));
            var eventMessage = EventMessage.from(objectMapper, message);
            if (eventMessage.isProtocol(DATA)) {
                var dataEvent = DataEventMessage.from(objectMapper, eventMessage, secretKey);
                dataEventService.handle(dataEvent);
            }
        } finally {
            MDC.clear();
            TimeUtils.sleep(30_000);
//            TimeUtils.sleep(60_000L);
        }
    }
}

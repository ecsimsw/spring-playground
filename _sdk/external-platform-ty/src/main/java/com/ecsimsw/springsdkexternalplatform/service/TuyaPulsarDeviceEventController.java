package com.ecsimsw.springsdkexternalplatform.service;

import com.ecsimsw.springsdkexternalplatform.domain.Protocol;
import com.ecsimsw.springsdkexternalplatform.dto.TuyaDeviceEventMessage;
import com.ecsimsw.springsdkexternalplatform.dto.PulsarEventMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Controller
public class TuyaPulsarDeviceEventController {

    @Value("${pulsar.event.accessKey}")
    private String secretKey;

    @Value("${pulsar.event.topic}")
    private String topic;

    @Value("${pulsar.event.subscriptionName}")
    private String subscriptionName;

    private final PulsarClient pulsarClient;
    private final Platform1DeviceEventHandler platform1DeviceEventHandler;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void consume() {
        var consumer = pulsarClient.newConsumer()
            .topic("persistent://" + topic)
            .subscriptionName(subscriptionName)
            .subscriptionType(SubscriptionType.Shared)
            .subscriptionInitialPosition(SubscriptionInitialPosition.Latest)
            .subscribe();

        while (!Thread.currentThread().isInterrupted()) {
            var msg = consumer.receive(1, TimeUnit.SECONDS);
            if (msg == null) {
                continue;
            }

            try {
                var eventMessage = PulsarEventMessage.from(objectMapper, msg);
                if (eventMessage.isProtocol(Protocol.DATA)) {
                    var tuyaDeviceEventMessage = TuyaDeviceEventMessage.from(objectMapper, eventMessage, secretKey);
                    var deviceEventMessage = tuyaDeviceEventMessage.toDeviceEventMessage();
                    platform1DeviceEventHandler.handle(deviceEventMessage);
                }
            } catch (Exception e){
                e.fillInStackTrace();
            } finally {
                consumer.acknowledge(msg);
            }
        }
    }
}

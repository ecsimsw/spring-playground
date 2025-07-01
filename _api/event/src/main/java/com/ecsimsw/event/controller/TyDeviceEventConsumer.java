package com.ecsimsw.event.controller;

import com.ecsimsw.event.service.DeviceEventHandler;
import com.ecsimsw.sdkty.dto.TyEventMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class TyDeviceEventConsumer {

    @Value("${ty.pulsar.event.secretKey}")
    private String secretKey;

    private final DeviceEventHandler deviceEventHandler;
    private final ObjectMapper objectMapper;

    @PulsarListener(
        topics = "${ty.pulsar.event.topic}",
        subscriptionName = "${ty.pulsar.event.subscriptionName}",
        subscriptionType = SubscriptionType.Shared,
        concurrency = "${ty.pulsar.event.partitionNumber}"
    )
    public void receiveMessage(Message<byte[]> message) {
        try {
            var eventMessage = TyEventMessage.from(objectMapper, message);
            if (eventMessage.isPairingEvent()) {
                var pairingEventMessage = eventMessage.asPairingMessage(objectMapper, secretKey);
                deviceEventHandler.handlePairingEvent(pairingEventMessage);
            }
            if (eventMessage.isDeviceEvent()) {
                var deviceEventMessage = eventMessage.asDeviceMessage(objectMapper, secretKey);
                deviceEventHandler.handleDeviceEvent(deviceEventMessage);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
}

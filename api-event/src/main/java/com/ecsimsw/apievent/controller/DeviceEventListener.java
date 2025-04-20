package com.ecsimsw.apievent.controller;

import com.ecsimsw.apievent.dto.EventMessage;
import com.ecsimsw.apievent.support.AESBase64Decrypt;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Controller
public class DeviceEventListener {

    private final ObjectMapper objectMapper;

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
            var eventMessage = objectMapper.readValue(message.getData(), EventMessage.class);
            var decryptedBody = AESBase64Decrypt.decrypt(eventMessage.data(), secretKey);
            System.out.println(decryptedBody);
        } catch (IOException e) {
            log.error("failed to consume event : {}", e.getMessage());
        }
    }
}

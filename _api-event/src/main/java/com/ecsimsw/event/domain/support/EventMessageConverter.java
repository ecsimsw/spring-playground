package com.ecsimsw.event.domain.support;

import com.ecsimsw.event.domain.EventMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.Message;

import java.io.IOException;

public class EventMessageConverter {

    public static EventMessage from(ObjectMapper objectMapper, Message<byte[]> message) {
        try {
            return objectMapper.readValue(message.getData(), EventMessage.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to consume event", e);
        }
    }
}

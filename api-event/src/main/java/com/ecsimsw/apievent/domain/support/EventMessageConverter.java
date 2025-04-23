package com.ecsimsw.apievent.domain.support;

import com.ecsimsw.apievent.domain.EventMessage;
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

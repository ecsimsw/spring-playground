package com.ecsimsw.event.domain;

import com.ecsimsw.event.domain.support.Protocol;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public record EventMessage(
    int protocol,
    Long t,
    String data
) {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventMessage.class);

    public static EventMessage from(ObjectMapper objectMapper, Message<byte[]> message) {
        try {
            return objectMapper.readValue(message.getData(), EventMessage.class);
        } catch (IOException e) {
            LOGGER.error("failed to parse event : {}", e.getMessage());
            throw new IllegalArgumentException("failed to consume event", e);
        }
    }

    public boolean isProtocol(Protocol protocol) {
        return this.protocol == protocol.id;
    }
}

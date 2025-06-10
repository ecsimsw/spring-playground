package com.ecsimsw.springsdkexternalplatform.dto;

import com.ecsimsw.springsdkexternalplatform.domain.Protocol;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public record PulsarEventMessage(
    int protocol,
    Long t,
    String data,
    String pv,
    String sign
) {

    private static final Logger LOGGER = LoggerFactory.getLogger(PulsarEventMessage.class);

    public static PulsarEventMessage from(ObjectMapper objectMapper, Message<byte[]> message) {
        try {
            return objectMapper.readValue(message.getData(), PulsarEventMessage.class);
        } catch (IOException e) {
            LOGGER.error("failed to parse event : {}", e.getMessage());
            throw new IllegalArgumentException("failed to consume event", e);
        }
    }

    public boolean isProtocol(Protocol protocol) {
        return this.protocol == protocol.id;
    }
}

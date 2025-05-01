package com.ecsimsw.event.domain;

import com.ecsimsw.event.support.AESBase64Decrypt;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public record StatusEventMessage(
    String devId,
    String pid,
    String bizCode,
    Map<String, String> bizData,
    Long ts
) {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusEventMessage.class);

    public static StatusEventMessage from(ObjectMapper objectMapper, EventMessage eventMessage, String secretKey) {
        try {
            var decryptedBody = AESBase64Decrypt.decrypt(eventMessage.data(), secretKey);
            return objectMapper.readValue(decryptedBody, StatusEventMessage.class);
        } catch (IOException e) {
            LOGGER.error("failed to parse event : {}", e.getMessage());
            throw new IllegalArgumentException("failed to consume event", e);
        }
    }
}

package com.ecsimsw.event.dto;

import com.ecsimsw.event.domain.EventMessage;
import com.ecsimsw.event.support.AESBase64Decrypt;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public record DeviceStatusEventMessage(
    String dataId,
    String devId,
    String productKey,
    List<Map<String, Object>> status
) {
    public static DeviceStatusEventMessage from(ObjectMapper objectMapper, EventMessage eventMessage, String secretKey) {
        try {
            var decryptedBody = AESBase64Decrypt.decrypt(eventMessage.data(), secretKey);
            return objectMapper.readValue(decryptedBody, DeviceStatusEventMessage.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to consume event", e);
        }
    }
}


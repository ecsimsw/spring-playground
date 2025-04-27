package com.ecsimsw.event.domain;

import com.ecsimsw.event.support.AESBase64Decrypt;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@Document(collection = "dataEvent")
public class DataEventMessage {

    @Id
    private String dataId;
    private String devId;
    private String productKey;
    private List<Map<String, Object>> status;
    private LocalDateTime timestamp = LocalDateTime.now();

    private static final Logger LOGGER = LoggerFactory.getLogger(DataEventMessage.class);

    public static DataEventMessage from(ObjectMapper objectMapper, EventMessage eventMessage, String secretKey) {
        try {
            var decryptedBody = AESBase64Decrypt.decrypt(eventMessage.data(), secretKey);
            return objectMapper.readValue(decryptedBody, DataEventMessage.class);
        } catch (IOException e) {
            LOGGER.error("failed to parse event : {}", e.getMessage());
            throw new IllegalArgumentException("failed to consume event", e);
        }
    }
}

package com.ecsimsw.device.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "device_history")
public class DeviceHistory {

    @Id
    private String id;

    private String deviceId;
    private String historyCode;
    private Object historyValue;

    @Indexed(name = "timestamp_idx", expireAfterSeconds = 3600)
    private LocalDateTime timestamp;

    public DeviceHistory(String deviceId, String historyCode, Object historyValue, LocalDateTime timestamp) {
        this(null, deviceId, historyCode, historyValue, timestamp);
    }
}
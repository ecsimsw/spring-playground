package com.ecsimsw.event.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@Document(collection = "device_alert")
public class DeviceAlertHistory {

    @Id
    private String id;
    private String deviceId;
    private String code;
    private Object value;
    private Instant timestamp = Instant.now(); // ttl indexed : 3600s

    public DeviceAlertHistory(String deviceId, String code, Object value) {
        this.deviceId = deviceId;
        this.code = code;
        this.value = value;
    }

    @Override
    public String toString() {
        return "DeviceAlertHistory{" +
            "id='" + id + '\'' +
            ", deviceId='" + deviceId + '\'' +
            ", tbCode='" + code + '\'' +
            ", spCode=" + value +
            ", timestamp=" + timestamp +
            '}';
    }
}

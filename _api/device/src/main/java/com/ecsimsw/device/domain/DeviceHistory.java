package com.ecsimsw.device.domain;

import com.ecsimsw.common.support.converter.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeviceHistory {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String deviceId;
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> history;
    private LocalDateTime timestamp;  // ttl index

    public DeviceHistory(String deviceId, Map<String, Object> history, LocalDateTime timestamp) {
        this(null, deviceId, history, timestamp);
    }
}
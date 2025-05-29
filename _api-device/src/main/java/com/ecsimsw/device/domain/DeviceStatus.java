package com.ecsimsw.device.domain;

import com.ecsimsw.common.domain.DeviceType;
import com.ecsimsw.device.support.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeviceStatus {

    @Id
    private String deviceId;

    @Enumerated(value = EnumType.STRING)
    private DeviceType deviceType;

    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> status;

    public void updateStatus(String code, Object value) {
        status.put(code, value);
    }
}

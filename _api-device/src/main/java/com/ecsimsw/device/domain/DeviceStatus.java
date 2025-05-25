package com.ecsimsw.device.domain;

import com.ecsimsw.device.support.MapToJsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    @Column(columnDefinition = "json")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> status;
}

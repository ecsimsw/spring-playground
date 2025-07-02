package com.ecsimsw.device.domain;

import com.ecsimsw.common.support.converter.MapToJsonConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BindDevice {

    @Id
    private String deviceId;
    private String username;
    private boolean online;
    private String name;
    private String productId;

    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> status;

    public void addStatus(String code, Object value) {
        status.put(code, value);
    }
}
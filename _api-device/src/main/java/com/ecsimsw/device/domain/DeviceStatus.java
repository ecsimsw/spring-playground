package com.ecsimsw.device.domain;

import com.ecsimsw.common.domain.Product;
import com.ecsimsw.common.support.converter.MapToJsonConverter;
import com.ecsimsw.common.support.converter.ProductConverter;
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

    @Convert(converter = ProductConverter.class)
    private Product product;

    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> status;

    public void updateStatus(String code, Object value) {
        if (product.hasStatusCode(code)) {
            status.put(code, value);
        }
    }
}

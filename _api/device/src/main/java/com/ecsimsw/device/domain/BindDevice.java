package com.ecsimsw.device.domain;

import com.ecsimsw.common.domain.Product;
import com.ecsimsw.common.domain.Products;
import com.ecsimsw.common.support.converter.MapToJsonConverter;
import com.ecsimsw.common.support.converter.ProductConverter;
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

    @Convert(converter = ProductConverter.class)
    private Product product;

    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> status;

    public BindDevice(
        String deviceId,
        String username,
        String productId,
        String name,
        boolean online
    ) {
        this(deviceId, username, online, name, Products.getById(productId), new HashMap<>());
    }

    public BindDevice(
        String deviceId,
        String username,
        Product product,
        String name,
        boolean online
    ) {
        this(deviceId, username, online, name, product, new HashMap<>());
    }

    public void addStatus(String code, Object value) {
        status.put(code, value);
    }
}
package com.ecsimsw.device.domain;

import com.ecsimsw.common.domain.Product;
import com.ecsimsw.common.domain.Products;
import com.ecsimsw.common.support.converter.ProductConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public BindDevice(
        String deviceId,
        String username,
        String productId,
        String name,
        boolean online
    ) {
        this(deviceId, username, online, name, Products.getById(productId));
    }
}

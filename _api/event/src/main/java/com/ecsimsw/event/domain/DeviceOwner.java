package com.ecsimsw.event.domain;

import com.ecsimsw.common.domain.Product;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DeviceOwner {

    @Id
    private String deviceId;
    private String username;

    @Convert(converter = ProductConverter.class)
    private Product product;
}

package com.ecsimsw.device.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String id;
    private String username;
    private String productId;
    private boolean online;

    @Enumerated(value = EnumType.STRING)
    private DeviceType type;

    public BindDevice(String id, String username, String productId, boolean online) {
        this.id = id;
        this.username = username;
        this.productId = productId;
        this.online = online;
        this.type = DeviceType.resolveByProductId(productId);
    }
}

package com.ecsimsw.event.domain;

import com.ecsimsw.common.domain.DeviceType;
import jakarta.persistence.*;
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
    private String productId;

    @Enumerated(value = EnumType.STRING)
    private DeviceType deviceType;
}

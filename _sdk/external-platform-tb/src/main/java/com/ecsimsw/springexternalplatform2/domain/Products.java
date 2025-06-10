package com.ecsimsw.springexternalplatform2.domain;

import com.ecsimsw.springexternalplatform2.domain.converter.*;
import com.ecsimsw.springsdkexternalplatform.dto.Command;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceStatus;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public enum Products {

    Plug("uxjr57hvapakd0io", new Tuxjr57hvapakd0ioConverter()),
    Brunt("mhf0rqd7uuvz6hf8", new Tmhf0rqd7uuvz6hf8Converter()),
    Camera("3cwbcqiz8qixphvu", new T3cwbcqiz8qixphvuConverter()),
    Power("hejspm_12C724", new Hhejspm_12C724Converter());

    public final String productId;
    public final StatusCommandConverter converter;

    public List<Command> toCommand(List<DeviceStatus> statusList) {
        return converter.toCommand(statusList);
    }

    public List<DeviceStatus> fromStatus(List<DeviceStatus> statusList) {
        return converter.fromStatus(statusList);
    }

    public static Products resolveById(String productId) {
        return Arrays.stream(values())
            .filter(v -> v.productId.equals(productId))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Not a valid device"));
    }

    public static boolean isSupported(String productId) {
        return Arrays.stream(values())
            .anyMatch(v -> v.productId.equals(productId));
    }
}

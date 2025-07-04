package com.ecsimsw.device.dto;

import com.ecsimsw.device.domain.BindDevice;
import com.ecsimsw.sdkcommon.domain.PlatformProducts;
import com.ecsimsw.sdkcommon.domain.ProductType;

import java.util.Map;

public record DeviceInfoResponse(
    String deviceId,
    ProductType productType,
    String productId,
    String name,
    boolean online,
    Map<String, Object> status
) {

    public static DeviceInfoResponse of(BindDevice device) {
        var product = PlatformProducts.getById(device.getProductId());
        return new DeviceInfoResponse(
            device.getDeviceId(),
            product.productType,
            product.id,
            device.getName(),
            device.isOnline(),
            device.getStatus()
        );
    }
}

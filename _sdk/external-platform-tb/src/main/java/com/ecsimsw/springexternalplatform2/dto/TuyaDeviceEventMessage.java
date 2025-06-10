package com.ecsimsw.springexternalplatform2.dto;

import com.ecsimsw.springsdkexternalplatform.domain.PlatformProducts;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceEventMessage;
import com.ecsimsw.springsdkexternalplatform.util.AESBase64Decrypt;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public record TuyaDeviceEventMessage(
    String dataId,
    String devId,
    String productKey,
    List<Map<String, Object>> status
) {
    public static TuyaDeviceEventMessage from(ObjectMapper objectMapper, PulsarEventMessage eventMessage, String secretKey) {
        try {
            var decryptedBody = AESBase64Decrypt.decrypt(eventMessage.data(), secretKey);
            return objectMapper.readValue(decryptedBody, TuyaDeviceEventMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("failed to consume event", e);
        }
    }

    public DeviceEventMessage toDeviceEventMessage() {
        var product = PlatformProducts.getById(productKey);
        var deviceStatus = new LinkedList<DeviceStatus>();
        for(var s : status) {
            var code = product.asSpCode((String)s.get("code"));
            var value = s.get("value");
            deviceStatus.add(new DeviceStatus(code, value));
        }
        return new DeviceEventMessage(devId, productKey, deviceStatus);
    }
}

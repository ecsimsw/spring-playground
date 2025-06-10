package com.ecsimsw.springexternalplatform2.service;

import com.ecsimsw.springexternalplatform2.domain.PlatformProducts;
import com.ecsimsw.springexternalplatform2.domain.PlatformType;
import com.ecsimsw.springexternalplatform2.domain.Products;
import com.ecsimsw.springexternalplatform2.dto.DeviceInfo;
import com.ecsimsw.springexternalplatform2.dto.DeviceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ThingsboardPlatformService {

    @Value("${mqtt.device.broker}")
    private String brokerEndpoint;

    public List<DeviceInfo> getDeviceListByUsername(String username) {
        return List.of(
            new DeviceInfo("POWER2", "POWER2", Products.Power.productId, true, List.of(new DeviceStatus("switch", false))),
            new DeviceInfo("POWER8", "POWER8", Products.Power.productId, true, List.of(new DeviceStatus("switch", false))),
            new DeviceInfo("POWER11", "POWER4", Products.Power.productId, true, List.of(new DeviceStatus("switch", false))),
            new DeviceInfo("POWER13", "POWER13", Products.Power.productId, true, List.of(new DeviceStatus("switch", false)))
        );
    }

    public void command(String deviceId, String productId, List<DeviceStatus> deviceStatuses) {
        if (!PlatformProducts.isSupported(productId)) {
            throw new IllegalArgumentException("Not a supported device");
        }
        var product = PlatformProducts.getById(productId);
        if (product.platformType() == PlatformType.Thingsboard) {
            try {
                var clientId = MqttClient.generateClientId();
                var client = new MqttClient(brokerEndpoint, clientId);
                client.connect();
                var topic = "cmnd/" + product.id() + "/" + deviceId;
                var message = new MqttMessage();
                message.setQos(1);
                message.setRetained(false);
                if (deviceStatuses.get(0).getValue().equals(true)) {
                    message.setPayload("ON".getBytes());
                } else {
                    message.setPayload("OFF".getBytes());
                }
                client.publish(topic, message);
                log.info("Message sent to topic: " + topic);
                client.disconnect();
            } catch (Exception e) {
                e.fillInStackTrace();
                throw new IllegalArgumentException();
            }
        }
    }
}
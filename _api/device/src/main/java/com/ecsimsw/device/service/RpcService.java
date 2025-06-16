package com.ecsimsw.device.service;

import com.ecsimsw.common.domain.ProductType;
import com.ecsimsw.common.dto.DeviceStatusValue;
import com.ecsimsw.common.service.TbRpcListener;
import com.ecsimsw.device.domain.BindDevice;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.sdkty.service.TyApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class RpcService {

    private static final Map<String, MqttClient> MQTT_CLIENTS = new HashMap<>();

    private final TyApiService tyApiService;
    private final TbRpcListener tbRpcListener;
    private final BindDeviceRepository bindDeviceRepository;
    private final ObjectMapper objectMapper;

    public void connect(String deviceId) {
        var bindDevice = bindDeviceRepository.findById(deviceId)
            .orElseThrow(() -> new IllegalArgumentException("Not a valid device"));
        if(bindDevice.getProduct().type() == ProductType.Brunt) {
            var mqttClient = tbRpcListener.listenTbRpc(deviceId, bruntRpcCallback(bindDevice));
            MQTT_CLIENTS.put(deviceId, mqttClient);
        }
    }

    private MqttCallback bruntRpcCallback(BindDevice bindDevice) {
        var deviceId = bindDevice.getDeviceId();
        var product = bindDevice.getProduct();
        return new MqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                try {
                    var payload = new String(message.getPayload());
                    log.info("recv RPC : {}", payload);
                    var payloadMap = objectMapper.readValue(payload, Map.class);
                    if(product.type() == ProductType.Brunt) {
                        var code = (String) payloadMap.get("method");
                        var params = payloadMap.get("params");
                        var deviceStatusValue = new DeviceStatusValue(code, params);
                        tyApiService.command(deviceId, product.id(), List.of(deviceStatusValue));
                    }
                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                log.info("connection lost");
                if (MQTT_CLIENTS.containsKey(deviceId)) {
                    var mqttClient = MQTT_CLIENTS.get(deviceId);
                    tbRpcListener.close(mqttClient);
                    MQTT_CLIENTS.remove(deviceId);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        };
    }
}

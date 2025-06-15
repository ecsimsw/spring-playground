package com.ecsimsw.device.service;

import com.ecsimsw.common.service.TbRpcListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class RpcService {

    private static final Map<String, MqttClient> MQTT_CLIENTS = new HashMap<>();

    private final TbRpcListener tbRpcListener;

    public void connect(String deviceId) {
        var mqttClient = tbRpcListener.listenTbRpc(deviceId, rpcCallback(deviceId));
        MQTT_CLIENTS.put(deviceId, mqttClient);
    }

    private MqttCallback rpcCallback(String deviceId) {
        return new MqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                try {
                    var payload = new String(message.getPayload());
                    log.info("Recv RPC : {}", payload);
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

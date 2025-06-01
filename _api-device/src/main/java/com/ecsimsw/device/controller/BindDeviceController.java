package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.common.support.annotation.InternalHandler;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.service.DeviceService;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceStatus;
import com.ecsimsw.springsdkexternalplatform.service.ExternalPlatformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BindDeviceController {

    private final ExternalPlatformService externalPlatformService;
    private final DeviceService deviceService;

    @InternalHandler
    @PostMapping("/api/device/beta/refresh/{username}")
    public ApiResponse<Void> refresh(@PathVariable String username) {
        var deviceInfos = externalPlatformService.getDeviceList(username);
        deviceService.refresh(username, deviceInfos);
        log.info("Refresh succeed : {}", username);
        return ApiResponse.success();
    }

    @GetMapping("/api/device/list")
    public ApiResponse<List<DeviceInfoResponse>> list(AuthUser authUser) {
        var result = deviceService.deviceList(authUser.username());
        return ApiResponse.success(result);
    }

    @PostMapping("/api/device/{deviceId}")
    public ApiResponse<Void> control(
        AuthUser authUser,
        @PathVariable String deviceId,
        @RequestBody List<DeviceStatus> deviceStatuses
    ) {
        var bindDevice = deviceService.getUserDevice(authUser.username(), deviceId);
        externalPlatformService.command(
            deviceId,
            bindDevice.productId(),
            deviceStatuses
        );
        return ApiResponse.success();
    }

    @PostMapping("/api/device/beta/mqtt/{nodeId}/on")
    public ApiResponse<Void> on(
        @PathVariable String nodeId
    ) {
        try {
            String broker = "tcp://hejdev1.goqual.com:1883";
            String clientId = MqttClient.generateClientId();
            IMqttClient client = new MqttClient(broker, clientId);
            client.connect();
            String topic = "cmnd/hejspm_12C724/POWER16";
            String payload = "ON";
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(1);
            message.setRetained(false);
            client.publish(topic, message);
            System.out.println("Message sent to topic: " + topic);
            client.disconnect();
            return ApiResponse.success();
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new IllegalArgumentException();
        }
    }

    @PostMapping("/api/device/beta/mqtt/{nodeId}/off")
    public ApiResponse<Void> off(
        @PathVariable String nodeId
    ) {
        try {
            String broker = "tcp://hejdev1.goqual.com:1883";
            String clientId = MqttClient.generateClientId();
            IMqttClient client = new MqttClient(broker, clientId);
            client.connect();
            String topic = "cmnd/hejspm_12C724/POWER16";
            String payload = "OFF";
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(1);
            message.setRetained(false);
            client.publish(topic, message);
            System.out.println("Message sent to topic: " + topic);
            client.disconnect();
            return ApiResponse.success();
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new IllegalArgumentException();
        }
    }
}

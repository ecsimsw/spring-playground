package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.common.support.annotation.InternalHandler;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.service.DeviceService;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceInfo;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceStatus;
import com.ecsimsw.springsdkexternalplatform.service.ExternalPlatformService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        deviceService.bindDevices(username, List.of(
            new DeviceInfo("POWER1", "POWER1", "hejspm_12C724", true, List.of(new DeviceStatus("switch", true))),
            new DeviceInfo("POWER2", "POWER2", "hejspm_12C724", true, List.of(new DeviceStatus("switch", true))),
            new DeviceInfo("POWER3", "POWER3", "hejspm_12C724", true, List.of(new DeviceStatus("switch", true))),
            new DeviceInfo("POWER4", "POWER4", "hejspm_12C724", true, List.of(new DeviceStatus("switch", true))),
            new DeviceInfo("POWER5", "POWER5", "hejspm_12C724", true, List.of(new DeviceStatus("switch", true)))
        ));
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
        if(bindDevice.deviceId().contains("hjprop")) {
            try {
                var broker = "tcp://hejdev1.goqual.com:1883";
                var clientId = MqttClient.generateClientId();
                var client = new MqttClient(broker, clientId);
                client.connect();
                var topic = "cmnd/hejspm_12C724/POWER" + deviceId.charAt(deviceId.length() - 1);
                var payload = "ON";
                if(deviceStatuses.get(0).getValue().equals(true)) {
                    payload = "ON";
                } else {
                    payload = "OFF";
                }
                var message = new MqttMessage(payload.getBytes());
                message.setQos(1);
                message.setRetained(false);
                client.publish(topic, message);
                log.info("Message sent to topic: " + topic);
                client.disconnect();
                return ApiResponse.success();
            } catch (Exception e) {
                e.fillInStackTrace();
                throw new IllegalArgumentException();
            }
        }
        externalPlatformService.command(
            deviceId,
            bindDevice.productId(),
            deviceStatuses
        );
        return ApiResponse.success();
    }
}

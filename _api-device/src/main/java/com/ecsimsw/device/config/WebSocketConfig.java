package com.ecsimsw.device.config;

import com.ecsimsw.device.controller.DeviceEventWebSocketController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final DeviceEventWebSocketController deviceEventWebSocketController;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        var webSocketHandlerRegistration = registry.addHandler(
            deviceEventWebSocketController,
            "/api/device/ws/device-events"
        );
        webSocketHandlerRegistration.setAllowedOrigins("*");
    }
}
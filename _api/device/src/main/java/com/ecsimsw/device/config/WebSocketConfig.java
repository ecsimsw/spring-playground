package com.ecsimsw.device.config;

import com.ecsimsw.device.service.DeviceEventWebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final DeviceEventWebSocketService deviceEventWebSocketService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        var webSocketHandlerRegistration = registry.addHandler(
            deviceEventWebSocketService,
            "/api/device/ws/device-events"
        );
        webSocketHandlerRegistration.setAllowedOrigins("*");
    }
}
package com.ecsimsw.event.config;

import com.ecsimsw.event.controller.DeviceAlertWebSocketController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        var webSocketHandlerRegistration = registry.addHandler(
            new DeviceAlertWebSocketController(),
            "/ws/device-events"
        );
        webSocketHandlerRegistration.setAllowedOrigins("*");
    }
}
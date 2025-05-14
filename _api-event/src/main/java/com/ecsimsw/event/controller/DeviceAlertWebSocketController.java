package com.ecsimsw.event.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class DeviceAlertWebSocketController extends TextWebSocketHandler {

    private final Map<String, List<WebSocketSession>> deviceSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@Nullable WebSocketSession session) {
        var deviceId = session.getId();
        var sessions = deviceSessions.computeIfAbsent(
            deviceId,
            ab -> Collections.synchronizedList(new ArrayList<>())
        );
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(@Nullable WebSocketSession session, @Nullable CloseStatus status) {
        var deviceId = session.getId();
        var sessions = deviceSessions.get(deviceId);
        if (sessions.isEmpty()) {
            return;
        }
        synchronized (sessions) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                deviceSessions.remove(deviceId);
            }
        }
    }

    public void alert(String deviceId, String message) throws IOException {
        var sessionsByDevice = deviceSessions.get(deviceId);
        sessionsByDevice.removeIf(session -> !session.isOpen());
        for (var session : sessionsByDevice) {
            session.sendMessage(new TextMessage(message));
        }
    }

    @Scheduled(fixedDelay = 10_000)
    public void cleanupClosedSessions() {
        for (Map.Entry<String, List<WebSocketSession>> entry : deviceSessions.entrySet()) {
            List<WebSocketSession> sessions = entry.getValue();
            synchronized (sessions) {
                sessions.removeIf(session -> !session.isOpen());
            }
            if (sessions.isEmpty()) {
                deviceSessions.remove(entry.getKey());
            }
        }
    }
}
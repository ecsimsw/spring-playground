package com.ecsimsw.device.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@EnableScheduling
@RequiredArgsConstructor
@Service
public class DeviceEventWebSocketService extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@Nullable WebSocketSession session) throws IOException {
        var username = session.getId();
        userSessions.put(username, session);
        session.sendMessage(new TextMessage("Successfully connected"));
        log.info("connected : {}", username);
    }

    @SneakyThrows
    public void sendMessage(String username, Object message) {
        var messageAsString = objectMapper.writeValueAsString(message);
        userSessions.values().stream()
            .filter(WebSocketSession::isOpen)
            .forEach(session -> {
                try {
                    session.sendMessage(new TextMessage(messageAsString));
                } catch (Exception e) {
                    // TODO :: 예외 처리
                    e.fillInStackTrace();
                    log.error("failed to send event : {}", e.getMessage());
                }
            });
    }

    @Override
    public void afterConnectionClosed(@Nullable WebSocketSession session, @Nullable CloseStatus status) throws IOException {
        var username = session.getId();
        var sessions = userSessions.get(username);
        synchronized (sessions) {
            session.sendMessage(new TextMessage("Successfully closed"));
            userSessions.remove(username);
            log.info("closed : {}", username);
        }
    }
//
//    @Scheduled(fixedDelay = 10_000)
//    public void cleanupClosedSessions() {
//        for (var userSession : userSessions.keySet()) {
//            synchronized (userSession) {
//                if(userSessions.)
//                userSessions.removeIf(userSession -> !userSession.isOpen());
//            }
//            if (sessions.isEmpty()) {
//                userSessions.remove(entry.getKey());
//            }
//        }
//    }
}
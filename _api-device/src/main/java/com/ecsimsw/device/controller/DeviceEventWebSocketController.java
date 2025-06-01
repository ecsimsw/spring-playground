package com.ecsimsw.device.controller;

import io.micrometer.common.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@EnableScheduling
@Slf4j
@Controller
public class DeviceEventWebSocketController extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@Nullable WebSocketSession session) throws IOException {
        var username = session.getId();
        userSessions.put(username, session);
        session.sendMessage(new TextMessage("Successfully connected"));
        log.info("connected : {}", username);
    }

    @Scheduled(fixedRate = 1000)
    public void sendHiMessageToAll() {
        try {
            for (var session : userSessions.values()) {
                if(session.isOpen()) {
                    log.info("send : {}", "hi");
                    session.sendMessage(new TextMessage("hi"));
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
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
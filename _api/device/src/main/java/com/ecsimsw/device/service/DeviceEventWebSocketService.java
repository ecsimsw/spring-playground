package com.ecsimsw.device.service;

import com.ecsimsw.common.dto.DeviceStatusUpdateEvent;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.error.DeviceException;
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
import org.springframework.web.util.UriComponentsBuilder;

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
        if(session.getUri() == null) {
            throw new DeviceException(ErrorType.INVALID_REQUEST, "Session doesn't have user info");
        }
        var username = UriComponentsBuilder.fromUri(session.getUri()).build().getQueryParams().getFirst("username");
        userSessions.put(username, session);
        session.sendMessage(new TextMessage("Successfully connected"));
        log.info("connected : {}", username);
    }

    @SneakyThrows
    public void sendStatus(String username, DeviceStatusUpdateEvent updateEvent) {
        try {
            if(!userSessions.containsKey(username)) {
                return;
            }
            var sessions = userSessions.get(username);
            sessions.sendMessage(new TextMessage(objectMapper.writeValueAsString(updateEvent)));
            log.info("[web socket] send status {} {}", username, updateEvent);
        } catch (Exception e) {
            e.fillInStackTrace();
            log.error("[web socket] failed to send event : {}", e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(@Nullable WebSocketSession session, @Nullable CloseStatus status) throws IOException {
        if(session.getUri() == null) {
            return;
        }
        var username = UriComponentsBuilder.fromUri(session.getUri()).build().getQueryParams().getFirst("username");
        var sessions = userSessions.get(username);
        synchronized (sessions) {
            userSessions.remove(username);
            log.info("closed : {}", username);
        }
    }
}
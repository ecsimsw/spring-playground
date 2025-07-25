package com.ecsimsw.device.service;

import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.domain.UserFcmToken;
import com.ecsimsw.device.domain.UserFcmTokenRepository;
import com.ecsimsw.device.error.NotificationException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmService {

    private final UserFcmTokenRepository userFcmTokenRepository;
    private final FirebaseMessaging firebaseMessaging;

    @Transactional
    public void add(String username, String token) {
        if(userFcmTokenRepository.existsByUsernameAndToken(username, token)) {
            return;
        }
        userFcmTokenRepository.save(new UserFcmToken(username, token));
    }

    @Transactional
    public void remove(String username, String token) {
        userFcmTokenRepository.deleteByUsernameAndToken(username, token);
    }

    @Transactional(readOnly = true)
    public List<String> findAll(String username) {
        return userFcmTokenRepository.findAllByUsername(username)
            .stream()
            .map(UserFcmToken::getToken)
            .toList();
    }

    public void sendMessage(String targetToken, String title, String body) {
        try {
            var message = Message.builder()
                .setToken(targetToken)
                .putData("title", title)
                .putData("body", body)
                .build();
            firebaseMessaging.send(message);
            log.info("Message sent to target token: {}", targetToken);
        } catch (FirebaseMessagingException e) {
            log.error("failed to send message : {} {} {}", targetToken, body, e.getMessage());
            throw new NotificationException(ErrorType.UNHANDLED);
        }
    }
}

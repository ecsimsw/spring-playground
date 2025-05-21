package com.ecsimsw.notification.service;

import com.ecsimsw.notification.domain.UserFcmToken;
import com.ecsimsw.notification.domain.UserFcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FcmService {

    private final UserFcmTokenRepository userFcmTokenRepository;

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
}

package com.ecsimsw.notification.service;

import com.ecsimsw.common.dto.DeviceAlertEvent;
import com.ecsimsw.notification.domain.UserFcmTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceAlertService {

    private final UserFcmTokenRepository fcmTokenRepository;
    private final FcmService fcmService;

    public void alert(DeviceAlertEvent alertEvent) {
        log.info("Handle alert event : {} {}", alertEvent.getDeviceId(), alertEvent.getCode());
        var bindOwner = alertEvent.getUsername();
        var fcmTokens = fcmTokenRepository.findAllByUsername(bindOwner);
        for (var fcmToken : fcmTokens) {
            fcmService.sendMessage(fcmToken.getToken(), "알림", "홈카메라에서 동작이 감지되었습니다.");
            log.info("sent fcm message {}, {}", fcmToken.getUsername(), alertEvent.getDeviceId());
        }
        // TODO :: 알림 전달 기록
        // TODO :: 읽음 확인
    }
}

package com.ecsimsw.device.service;

import com.ecsimsw.common.dto.DeviceAlertEvent;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.device.domain.UserFcmTokenRepository;
import com.ecsimsw.sdkcommon.domain.PlatformProducts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceAlertService {

    private final BindDeviceRepository deviceRepository;
    private final UserFcmTokenRepository fcmTokenRepository;
    private final FcmService fcmService;

    public void alert(DeviceAlertEvent alertEvent) {
        log.info("Handle alert event : {} {}", alertEvent.deviceId(), alertEvent.code());
        var bindOwner = alertEvent.username();
        var product = PlatformProducts.getById(alertEvent.productId());
        var alertMessage = product.alertCommonCodes.fromCommonCode(alertEvent.code()).alertMessage();
        var optBindDevice = deviceRepository.findByDeviceId(alertEvent.deviceId());
        if(optBindDevice.isEmpty()) {
            return;
        }
        var bindDevice = optBindDevice.orElseThrow();
        var title = String.format(alertMessage.titleFormat);
        var body = String.format(alertMessage.bodyFormat, bindDevice.getName());
        fcmTokenRepository.findAllByUsername(bindOwner).forEach(
            fcmToken -> {
                fcmService.sendMessage(fcmToken.getToken(), title, body);
                log.info("sent fcm message {}, {}", fcmToken.getUsername(), alertEvent.deviceId());
            }
        );
    }
}

package com.ecsimsw.device.service;

import org.springframework.stereotype.Service;

@Service
public class MqttBetaService {
    public void processMqttMessage(String topic, String payload) {
        // 원하는 로직으로 확장
        // 예: DB에 저장, 알림, 상태 저장 등
    }
}
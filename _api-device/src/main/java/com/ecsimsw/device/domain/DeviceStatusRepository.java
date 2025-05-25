package com.ecsimsw.device.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, String> {

    void deleteByDeviceId(String deviceId);
}

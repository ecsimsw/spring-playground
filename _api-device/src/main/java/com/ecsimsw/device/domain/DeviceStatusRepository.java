package com.ecsimsw.device.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, String> {

    void deleteAllByDeviceIdIn(List<String> deviceIds);

    Optional<DeviceStatus> findByDeviceId(String deviceId);
    
    List<DeviceStatus> findAllByDeviceIdIn(List<String> deviceIds);
}

package com.ecsimsw.device.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BindDeviceRepository extends JpaRepository<BindDevice, String> {

    List<BindDevice> findAllByUsername(String username);

    void deleteAllByUsername(String username);

    boolean existsByDeviceIdAndUsername(String deviceId, String username);

    Optional<BindDevice> findByUsernameAndDeviceId(String username, String deviceId);
}

package com.ecsimsw.device.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BindDeviceRepository extends JpaRepository<BindDevice, String> {

    List<BindDevice> findAllByUsername(String username);

    void deleteAllByUsername(String username);

}

package com.ecsimsw.device.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BindDeviceRepository extends JpaRepository<BindDevice, Long> {

    List<BindDevice> findAllByUsername(String username);

}

package com.ecsimsw.event.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceOwnerRepository extends JpaRepository<DeviceOwner, String> {

    void deleteAllByUsername(String username);
}
